//
// --------------------------------------------------------------------------
//  Gurux Ltd

package gurux.dlms.asn;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXSimpleEntry;
import gurux.dlms.asn.enums.X509Name;
import gurux.dlms.asn.enums.X9ObjectIdentifier;
import gurux.dlms.internal.GXCommon;

/*
 * ASN1 converter. This class is used to convert 
 * public and private keys to byte array and vice verse.
 */
public final class GXAsn1Converter {
    /*
     * Constructor.
     */
    private GXAsn1Converter() {

    }

    /**
     * Convert bytes to ASN.1 DER.
     * 
     * @param data
     *            bytes to convert.
     * @return ASN.1 DER
     */
    public static byte[] encode(final byte[] data) {
        return data;
    }

    /**
     * Convert ASN.1 DER to bytes.
     * 
     * @param der
     *            ASN.1 DER bytes.
     * @return bytes from DER structure.
     */
    public static byte[] getBytes(final byte[] der) {
        return der;
        // GXByteBuffer bb = new GXByteBuffer(der.length);
        // int len;
        // for (int pos = 0; pos != der.length; ++pos) {
        // byte it = der[pos];
        // // Get length.
        // len = length(der, ++pos);
        // switch (it) {
        // case BerType.CONSTRUCTED | BerType.SEQUENCE:
        // break;
        // case BerType.INTEGER:
        // bb.set(der, pos + 1, len);
        // pos += len;
        // break;
        // default:
        // throw new RuntimeException("Invalid data.");
        // }
        // }
        // return bb.array();
    }

    public static PublicKey getPublicKey(final X9ObjectIdentifier algorithm,
            final X9ObjectIdentifier parameters, final GXAsn1PublicKey key) {
        byte[] encodedKey = GXAsn1Converter.toByteArray(new Object[] {
                new Object[] { new GXAsn1ObjectIdentifier(algorithm.getValue()),
                        new GXAsn1ObjectIdentifier(parameters.getValue()) },
                key });
        KeyFactory eckf;
        try {
            eckf = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "EC key factory not present in runtime");
        }
        try {
            X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
            return eckf.generatePublic(ecpks);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static List<GXSimpleEntry<Object, Object>>
            encodeSubject(final String value) {
        X509Name name;
        Object val;
        List<GXSimpleEntry<Object, Object>> list =
                new ArrayList<GXSimpleEntry<Object, Object>>();
        List<String> arr1 = GXCommon.split(value, ',');
        for (int pos = 0; pos != arr1.size(); ++pos) {
            List<String> it = GXCommon.split(arr1.get(pos), '=');
            if (it.size() != 2) {
                throw new IllegalArgumentException("Invalid subject.");
            }
            name = X509Name.valueOf(it.get(0).trim());
            if (name == X509Name.C) {
                // Country code is printable string
                val = it.get(1).trim();
            } else if (name == X509Name.E) {
                // email address in Verisign certificates
                val = new GXAsn1Ia5String(it.get(1).trim());
            } else {
                val = new GXAsn1Utf8String(it.get(1).trim());
            }
            String oid = name.getValue();
            list.add(new GXSimpleEntry<Object, Object>(
                    new GXAsn1ObjectIdentifier(oid), val));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    static String getSubject(final GXAsn1Sequence values) {
        Object value;
        StringBuilder sb = new StringBuilder();
        for (Object tmp : values) {
            Map.Entry<Object, Object> it = (Map.Entry<Object, Object>) tmp;
            sb.append(X509Name.forValue(it.getKey().toString()));
            sb.append('=');
            value = it.getValue();
            sb.append(value);
            sb.append(", ");
        }
        // Remove last comma.
        if (sb.length() != 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private static void getValue(final GXByteBuffer bb,
            final List<Object> objects) {
        int type, len;
        List<Object> tmp = null;
        byte[] tmp2;
        type = bb.getUInt8();
        len = GXCommon.getObjectCount(bb);
        if (len > bb.size() - bb.position()) {
            throw new IllegalArgumentException("Not enought memory.");
        }
        int start = bb.position();
        switch (type) {
        case BerType.CONSTRUCTED | BerType.CONTEXT:
            tmp = new GXAsn1Context();
            objects.add(tmp);
            while (bb.position() < start + len) {
                getValue(bb, tmp);
            }
            break;
        case BerType.CONSTRUCTED | BerType.SEQUENCE:
            tmp = new GXAsn1Sequence();
            objects.add(tmp);
            while (bb.position() < start + len) {
                getValue(bb, tmp);
            }
            break;
        case BerType.CONSTRUCTED | BerType.SET:
            tmp = new ArrayList<Object>();
            getValue(bb, tmp);
            tmp = (GXAsn1Sequence) tmp.get(0);
            objects.add(
                    new GXSimpleEntry<Object, Object>(tmp.get(0), tmp.get(1)));
            break;
        case BerType.OBJECT_IDENTIFIER:
            objects.add(new GXAsn1ObjectIdentifier(bb, len));
            break;
        case BerType.PRINTABLE_STRING:
            objects.add(bb.getString(len));
            break;
        case BerType.UTF8STRING:
            objects.add(new GXAsn1Utf8String(
                    bb.getString(bb.position(), len, "UTF-8")));
            bb.position(bb.position() + len);
            break;
        case BerType.IA5_STRING:
            objects.add(new GXAsn1Ia5String(bb.getString(len)));
            break;
        case BerType.INTEGER:
            if (len == 1) {
                objects.add(new Byte(bb.getInt8()));
            } else if (len == 2) {
                objects.add(new Short(bb.getInt16()));
            } else if (len == 4) {
                objects.add(new Integer(bb.getInt32()));
            } else {
                tmp2 = new byte[len];
                bb.get(tmp2);
                objects.add(new GXAsn1Integer(tmp2));
            }
            break;
        case BerType.NULL:
            objects.add(null);
            break;
        case BerType.BIT_STRING:
            objects.add(new GXAsn1BitString(bb.subArray(bb.position(), len)));
            bb.position(bb.position() + len);
            break;
        case BerType.UTC_TIME:
            tmp2 = new byte[len];
            bb.get(tmp2);
            objects.add(getDate(new String(tmp2)));
            break;
        default:
            throw new RuntimeException("Invalid type: " + type);
        }
    }

    private static Date getDate(final String dateString) {
        int year, month, day, hour, minute, second = 0;
        Calendar calendar;
        year = 2000 + Integer.parseInt(dateString.substring(0, 2));
        month = Integer.parseInt(dateString.substring(2, 4)) - 1;
        day = Integer.parseInt(dateString.substring(4, 6));
        hour = Integer.parseInt(dateString.substring(6, 8));
        minute = Integer.parseInt(dateString.substring(8, 10));
        if (dateString.endsWith("Z")) {
            if (dateString.length() > 11) {
                second = Integer.parseInt(dateString.substring(10, 12));
            }
            calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        } else {
            if (dateString.length() > 15) {
                second = Integer.parseInt(dateString.substring(10, 12));
            }
            calendar = Calendar.getInstance(TimeZone.getTimeZone(
                    "GMT" + dateString.substring(dateString.length() - 6,
                            dateString.length() - 1)));
        }
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }

    private static String dateToString(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long v = calendar.getTimeInMillis();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(v);
        StringBuffer sb = new StringBuffer();
        sb.append(
                GXCommon.integerString(calendar.get(Calendar.YEAR) - 2000, 2));
        sb.append(GXCommon.integerString(1 + calendar.get(Calendar.MONTH), 2));
        sb.append(
                GXCommon.integerString(calendar.get(Calendar.DAY_OF_MONTH), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.HOUR), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.MINUTE), 2));
        sb.append(GXCommon.integerString(calendar.get(Calendar.SECOND), 2));
        sb.append("Z");
        return sb.toString();
    }

    /**
     * Convert byte array to ASN1 objects.
     * 
     * @param data
     * @return
     */
    public static Object fromByteArray(final byte[] data) {
        GXByteBuffer bb = new GXByteBuffer(data);
        List<Object> objects = new ArrayList<Object>();
        while (bb.position() != bb.size()) {
            getValue(bb, objects);
        }
        return objects.get(0);
    }

    /**
     * Add ASN1 object to byte buffer.
     * 
     * @param bb
     *            Byte buffer where ANS1 object is serialized.
     * @param target
     *            ANS1 object
     * @return Size of object.
     */
    @SuppressWarnings("unchecked")
    private static int getBytes(final GXByteBuffer bb, final Object target) {
        GXByteBuffer tmp;
        String str;
        int start = bb.size();
        int cnt = 0;
        if (target instanceof GXAsn1Context) {
            tmp = new GXByteBuffer();
            for (Object it : ((GXAsn1Context) target)) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.CONTEXT);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof Object[]) {
            tmp = new GXByteBuffer();
            for (Object it : (Object[]) target) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof List) {
            tmp = new GXByteBuffer();
            for (Object it : (List<Object>) target) {
                cnt += getBytes(tmp, it);
            }
            start = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, bb);
            cnt += bb.size() - start;
            bb.set(tmp);
            return cnt;
        } else if (target instanceof String) {
            bb.setUInt8(BerType.PRINTABLE_STRING);
            GXCommon.setObjectCount(((String) target).length(), bb);
            bb.add(target);
        } else if (target instanceof Byte) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(1, bb);
            bb.add(target);
        } else if (target instanceof Short) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(2, bb);
            bb.add(target);
        } else if (target instanceof Integer) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(4, bb);
            bb.add(target);
        } else if (target instanceof GXAsn1Integer) {
            bb.setUInt8(BerType.INTEGER);
            byte[] b = ((GXAsn1Integer) target).getByteArray();
            GXCommon.setObjectCount(b.length, bb);
            bb.set(b);
        } else if (target instanceof Long) {
            bb.setUInt8(BerType.INTEGER);
            GXCommon.setObjectCount(8, bb);
            bb.add(target);
        } else if (target instanceof byte[]) {
            bb.setUInt8(BerType.OCTET_STRING);
            GXCommon.setObjectCount(((byte[]) target).length, bb);
            bb.add(target);
        } else if (target == null) {
            bb.setUInt8(BerType.NULL);
            GXCommon.setObjectCount(0, bb);
        } else if (target instanceof GXAsn1ObjectIdentifier) {
            bb.setUInt8(BerType.OBJECT_IDENTIFIER);
            byte[] t = ((GXAsn1ObjectIdentifier) target).getEncoded();
            GXCommon.setObjectCount(t.length, bb);
            bb.add(t);
        } else if (target instanceof Entry) {
            Entry<Object, Object> e = (Entry<Object, Object>) target;
            GXByteBuffer tmp2 = new GXByteBuffer();
            cnt += getBytes(tmp2, e.getKey());
            cnt += getBytes(tmp2, e.getValue());
            tmp = new GXByteBuffer();
            tmp.setUInt8(BerType.CONSTRUCTED | BerType.SEQUENCE);
            GXCommon.setObjectCount(cnt, tmp);
            tmp.set(tmp2);
            // Update len.
            cnt = bb.size();
            bb.setUInt8(BerType.CONSTRUCTED | BerType.SET);
            GXCommon.setObjectCount(tmp.size(), bb);
            bb.set(tmp);
            return bb.size() - cnt;
        } else if (target instanceof GXAsn1Utf8String) {
            bb.setUInt8(BerType.UTF8STRING);
            str = target.toString();
            GXCommon.setObjectCount(str.length(), bb);
            bb.add(str);
        } else if (target instanceof GXAsn1Ia5String) {
            bb.setUInt8(BerType.IA5_STRING);
            str = target.toString();
            GXCommon.setObjectCount(str.length(), bb);
            bb.add(str);
        } else if (target instanceof GXAsn1BitString) {
            GXAsn1BitString bs = (GXAsn1BitString) target;
            bb.setUInt8(BerType.BIT_STRING);
            GXCommon.setObjectCount(1 + bs.getValue().length, bb);
            bb.setUInt8(bs.getPadBits());
            bb.add(bs.getValue());
        } else if (target instanceof GXAsn1PublicKey) {
            GXAsn1PublicKey bs = (GXAsn1PublicKey) target;
            bb.setUInt8(BerType.BIT_STRING);
            // Size is 64 bytes + padding and uncompressed point indicator.
            GXCommon.setObjectCount(66, bb);
            // Add padding.
            bb.setUInt8(0);
            // prefixed with the uncompressed point indicator 04
            bb.setUInt8(4);
            bb.add(bs.getValue());
            // Count is type + size + 64 bytes + padding + uncompressed point
            // indicator.
            return 68;
        } else if (target instanceof Date) {
            // Save date time in UTC.
            bb.setUInt8(BerType.UTC_TIME);
            str = dateToString((Date) target);
            bb.setUInt8(str.length());
            bb.add(str);
        } else {
            throw new RuntimeException(
                    "Invalid type: " + target.getClass().toString());
        }
        return bb.size() - start;
    }

    /**
     * Convert ASN1 objects to byte array.
     * 
     * @param data
     * @return
     */
    public static byte[] toByteArray(final Object data) {
        GXByteBuffer bb = new GXByteBuffer();
        getBytes(bb, data);
        return bb.array();
    }
}