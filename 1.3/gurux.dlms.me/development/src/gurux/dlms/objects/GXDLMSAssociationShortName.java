//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.objects;

import java.security.Signature;
import java.util.Enumeration;
import java.util.Vector;

import com.oracle.util.logging.Level;
import com.oracle.util.logging.Logger;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.AccessMode;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.MethodAccessMode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.manufacturersettings.GXDLMSAttributeSettings;
import gurux.dlms.secure.GXSecure;

public class GXDLMSAssociationShortName extends GXDLMSObject
		implements IGXDLMSBase {
	private static final Logger LOGGER = Logger
			.getLogger(GXDLMSAssociationShortName.class.getName());
	private Object accessRightsList;
	private GXDLMSObjectCollection objectList;
	private String securitySetupReference;

	/**
	 * Secret used in LLS Authentication.
	 */
	private byte[] llsSecret;

	/**
	 * Secret used in HLS Authentication.
	 */
	private byte[] hlsSecret;

	/**
	 * Constructor.
	 */
	public GXDLMSAssociationShortName() {
		this("0.0.40.0.0.255", 0xFA00);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 * @param sn
	 *            Short Name of the object.
	 */
	public GXDLMSAssociationShortName(final String ln, final int sn) {
		super(ObjectType.ASSOCIATION_SHORT_NAME, ln, sn);
		objectList = new GXDLMSObjectCollection(this);
		llsSecret = "Gurux".getBytes();
		hlsSecret = "Gurux".getBytes();
	}

	/**
	 * @return Secret used in LLS Authentication.
	 */
	public final byte[] getSecret() {
		return llsSecret;
	}

	/**
	 * @param value
	 *            Secret used in LLS Authentication.
	 */
	public final void setSecret(final byte[] value) {
		llsSecret = value;
	}

	/**
	 * @return Secret used in HLS Authentication.
	 */
	public final byte[] getHlsSecret() {
		return hlsSecret;
	}

	/**
	 * @param value
	 *            Secret used in HLS Authentication.
	 */
	public final void setHlsSecret(final byte[] value) {
		hlsSecret = value;
	}

	public final GXDLMSObjectCollection getObjectList() {
		return objectList;
	}

	public final Object getAccessRightsList() {
		return accessRightsList;
	}

	public final void setAccessRightsList(final Object value) {
		accessRightsList = value;
	}

	public final String getSecuritySetupReference() {
		return securitySetupReference;
	}

	public final void setSecuritySetupReference(final String value) {
		securitySetupReference = value;
	}

	public final Object[] getValues() {
		return new Object[] { getLogicalName(), getObjectList(),
				getAccessRightsList(), getSecuritySetupReference() };
	}

	/*
	 * Invokes method.
	 * 
	 * @param index Method index.
	 */

	public final byte[] invoke(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		// Check reply_to_HLS_authentication
		if (e.getIndex() == 8) {
			byte[] serverChallenge = null, clientChallenge = null;
			long ic = 0;
			byte[] readSecret;
			boolean accept = false;
			if (settings.getAuthentication() == Authentication.HIGH_ECDSA) {
				try {
//					GXByteBuffer signature = new GXByteBuffer(
//							(byte[]) e.getParameters());
					Signature ver = Signature.getInstance("SHA256withECDSA");
					// ver.initVerify(
					// settings.getCertificates().elementAt(0).getPublicKey());
					GXByteBuffer bb = new GXByteBuffer();
					bb.set(settings.getSourceSystemTitle());
					bb.set(settings.getCipher().getSystemTitle());
					bb.set(settings.getStoCChallenge());
					bb.set(settings.getCtoSChallenge());
					ver.update(bb.array(), 0, bb.size());
//					accept = ver
//							.verify(GXAsn1Converter.encode(signature.array()));

				} catch (Exception ex) {
					throw new RuntimeException(ex.getMessage());
				}
			} else {
				if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
					readSecret = settings.getSourceSystemTitle();
					GXByteBuffer bb = new GXByteBuffer(
							(byte[]) e.getParameters());
					bb.getUInt8();
					ic = bb.getUInt32();
				} else {
					readSecret = hlsSecret;
				}
				serverChallenge = GXSecure.secure(settings,
						settings.getCipher(), ic, settings.getStoCChallenge(),
						readSecret);
				clientChallenge = (byte[]) e.getParameters();
				accept = GXCommon.compare(serverChallenge, clientChallenge);
			}
			if (accept) {
				if (settings.getAuthentication() == Authentication.HIGH_GMAC) {
					readSecret = settings.getCipher().getSystemTitle();
					ic = settings.getCipher().getFrameCounter();
				} else {
					readSecret = hlsSecret;
				}
				settings.setConnected(true);
				return GXSecure.secure(settings, settings.getCipher(), ic,
						settings.getCtoSChallenge(), readSecret);
			} else {
				LOGGER.log(Level.INFO,
						"Invalid CtoS:" + GXCommon.toHex(serverChallenge, false)
								+ "-" + GXCommon.toHex(clientChallenge, false));
				return null;
			}
		} else {
			settings.setConnected(false);
			e.setError(ErrorCode.READ_WRITE_DENIED);
			return null;
		}
	}

	/*
	 * Returns collection of attributes to read. If attribute is static and
	 * already read or device is returned HW error it is not returned.
	 */

	public final int[] getAttributeIndexToRead() {
		Vector attributes = new Vector();
		// LN is static and read only once.
		if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
			attributes.addElement(new Integer(1));
		}
		// ObjectList is static and read only once.
		if (!isRead(2)) {
			attributes.addElement(new Integer(2));
		}
		// AccessRightsList is static and read only once.
		if (!isRead(3)) {
			attributes.addElement(new Integer(3));
		}
		// SecuritySetupReference is static and read only once.
		if (!isRead(4)) {
			attributes.addElement(new Integer(4));
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	public final int getAttributeCount() {
		return 4;
	}

	/*
	 * Returns amount of methods.
	 */

	public final int getMethodCount() {
		return 8;
	}

	private void getAccessRights(final GXDLMSObject item,
			final GXByteBuffer data) {
		data.setUInt8((byte) DataType.STRUCTURE.getValue());
		data.setUInt8((byte) 3);
		GXCommon.setData(data, DataType.UINT16,
				new Integer(item.getShortName()));
		data.setUInt8((byte) DataType.ARRAY.getValue());
		data.setUInt8((byte) item.getAttributes().size());
		Enumeration iterator = item.getAttributes().elements();
		while (iterator.hasMoreElements()) {
			GXDLMSAttributeSettings att = (GXDLMSAttributeSettings) iterator
					.nextElement();
			// attribute_access_item
			data.setUInt8((byte) DataType.STRUCTURE.getValue());
			data.setUInt8((byte) 3);
			GXCommon.setData(data, DataType.INT8, new Integer(att.getIndex()));
			GXCommon.setData(data, DataType.ENUM,
					new Integer(att.getAccess().getValue()));
			GXCommon.setData(data, DataType.NONE, null);
		}
		data.setUInt8((byte) DataType.ARRAY.getValue());
		data.setUInt8((byte) item.getMethodAttributes().size());
		iterator = item.getMethodAttributes().elements();
		while (iterator.hasMoreElements()) {
			GXDLMSAttributeSettings it = (GXDLMSAttributeSettings) iterator
					.nextElement();
			// attribute_access_item
			data.setUInt8((byte) DataType.STRUCTURE.getValue());
			data.setUInt8((byte) 2);
			GXCommon.setData(data, DataType.INT8, new Integer(it.getIndex()));
			GXCommon.setData(data, DataType.ENUM,
					new Integer(it.getMethodAccess().getValue()));
		}
	}

	public final DataType getDataType(final int index) {
		if (index == 1) {
			return DataType.OCTET_STRING;
		} else if (index == 2) {
			return DataType.ARRAY;
		} else if (index == 3) {
			return DataType.ARRAY;
		} else if (index == 4) {
			return DataType.OCTET_STRING;
		}
		throw new IllegalArgumentException(
				"getDataType failed. Invalid attribute index.");
	}

	/**
	 * Returns Association View.
	 */
	private byte[] getObjects(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		GXByteBuffer bb = new GXByteBuffer();
		int cnt = objectList.size();

		// Add count only for first time.
		if (settings.getIndex() == 0) {
			settings.setCount(cnt);
			bb.setUInt8((byte) DataType.ARRAY.getValue());
			// Add count
			GXCommon.setObjectCount(cnt, bb);
		}
		int pos = 0;
		if (cnt != 0) {
			Enumeration iterator = objectList.elements();
			while (iterator.hasMoreElements()) {
				GXDLMSObject it = (GXDLMSObject) iterator.nextElement();
				++pos;
				if (!(pos <= settings.getIndex())) {
					bb.setUInt8((byte) DataType.STRUCTURE.getValue());
					// Count
					bb.setUInt8((byte) 4);
					// base address.
					GXCommon.setData(bb, DataType.INT16,
							new Integer(it.getShortName()));
					// ClassID
					GXCommon.setData(bb, DataType.UINT16,
							new Integer(it.getObjectType().getValue()));
					// Version
					GXCommon.setData(bb, DataType.UINT8, new Integer(0));
					// LN
					GXCommon.setData(bb, DataType.OCTET_STRING,
							it.getLogicalName());
					settings.setIndex(settings.getIndex() + 1);
					// If PDU is full.
					if (!e.isSkipMaxPduSize()
							&& bb.size() >= settings.getMaxPduSize()) {
						break;
					}
				}
			}
		}
		return bb.array();
	}

	/*
	 * Returns value of given attribute.
	 */

	public final Object getValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		GXByteBuffer bb = new GXByteBuffer();
		if (e.getIndex() == 1) {
			return getLogicalName();
		} else if (e.getIndex() == 2) {
			return getObjects(settings, e);
		} else if (e.getIndex() == 3) {
			boolean lnExists = objectList.findBySN(this.getShortName()) != null;
			// Add count
			int cnt = objectList.size();
			if (!lnExists) {
				++cnt;
			}
			bb.setUInt8(DataType.ARRAY.getValue());
			GXCommon.setObjectCount(cnt, bb);
			Enumeration iterator = objectList.elements();
			while (iterator.hasMoreElements()) {
				GXDLMSObject it = (GXDLMSObject) iterator.nextElement();
				getAccessRights(it, bb);
			}
			if (!lnExists) {
				getAccessRights(this, bb);
			}
			return bb.array();
		} else if (e.getIndex() == 4) {
			return GXCommon.getBytes(securitySetupReference);
		}
		e.setError(ErrorCode.READ_WRITE_DENIED);
		return null;
	}

	final void updateAccessRights(final Object[] buff) {
		for (int pos = 0; pos != buff.length; ++pos) {
			Object access = buff[pos];
			Object[] it = (Object[]) access;
			int sn = GXCommon.intValue(it[0]);
			GXDLMSObject obj = objectList.findBySN(sn);
			if (obj != null) {
				for (int pos1 = 0; pos1 != ((Object[]) it[1]).length; ++pos1) {
					Object attributeAccess = it[1];
					int id = GXCommon.intValue(((Object[]) attributeAccess)[0]);
					int tmp = GXCommon
							.intValue(((Object[]) attributeAccess)[1]);
					AccessMode mode = AccessMode.forValue(tmp);
					obj.setAccess(id, mode);
				}
				for (int pos1 = 0; pos1 != ((Object[]) it[2]).length; ++pos1) {
					Object methodAccess = it[2];
					int id = GXCommon.intValue(((Object[]) methodAccess)[0]);
					int tmp = GXCommon.intValue(((Object[]) methodAccess)[1]);
					MethodAccessMode mode = MethodAccessMode.forValue(tmp);
					obj.setMethodAccess(id, mode);
				}
			}
		}
	}

	/*
	 * Set value of given attribute.
	 */

	public final void setValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			super.setValue(settings, e);
		} else if (e.getIndex() == 2) {
			objectList.removeAllElements();
			if (e.getValue() != null) {
				Object[] arr = (Object[]) e.getValue();
				for (int pos = 0; pos != arr.length; ++pos) {
					Object[] item = (Object[]) arr[pos];
					int sn = GXCommon.intValue(item[0]) & 0xFFFF;
					ObjectType type = ObjectType
							.forValue(GXCommon.intValue(item[1]));
					int version = GXCommon.intValue(item[2]);
					String ln = GXDLMSObject.toLogicalName((byte[]) item[3]);
					GXDLMSObject obj = gurux.dlms.GXDLMSClient
							.createObject(type);
					obj.setLogicalName(ln);
					obj.setShortName(sn);
					obj.setVersion(version);
					objectList.addElement(obj);
				}
			}
		} else if (e.getIndex() == 3) {
			if (e.getValue() == null) {
				Enumeration iterator = objectList.elements();
				while (iterator.hasMoreElements()) {
					GXDLMSObject it = (GXDLMSObject) iterator.nextElement();
					for (int pos = 1; pos != it.getAttributeCount(); ++pos) {
						it.setAccess(pos, AccessMode.NO_ACCESS);
					}
				}
			} else {
				updateAccessRights((Object[]) e.getValue());
			}
		} else if (e.getIndex() == 4) {
			if (e.getValue() instanceof String) {
				securitySetupReference = e.getValue().toString();
			} else if (e.getValue() != null) {
				securitySetupReference = new String((byte[]) e.getValue());
			}
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}
}