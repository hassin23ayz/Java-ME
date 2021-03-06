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

import java.util.Vector;

import gurux.dlms.GXByteBuffer;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.GXDLMSPppSetupIPCPOptionType;
import gurux.dlms.objects.enums.GXDLMSPppSetupLcpOptionType;
import gurux.dlms.objects.enums.PppAuthenticationType;

public class GXDLMSPppSetup extends GXDLMSObject implements IGXDLMSBase {
	private GXDLMSPppSetupIPCPOption[] ipcpOptions;
	private String phyReference;
	private GXDLMSPppSetupLcpOption[] lcpOptions;
	private byte[] userName;
	private byte[] password;
	private PppAuthenticationType authentication;

	public final PppAuthenticationType getAuthentication() {
		return authentication;
	}

	public final void setAuthentication(final PppAuthenticationType value) {
		authentication = value;
	}

	/**
	 * @return PPP authentication procedure user name.
	 */
	public final byte[] getUserName() {
		return userName;
	}

	/**
	 * @param value
	 *            PPP authentication procedure user name.
	 */
	public final void setUserName(final byte[] value) {
		userName = value;
	}

	/**
	 * @return PPP authentication procedure password.
	 */
	public final byte[] getPassword() {
		return password;
	}

	/**
	 * @param value
	 *            PPP authentication procedure password.
	 */
	public final void setPassword(final byte[] value) {
		password = value;
	}

	/**
	 * Constructor.
	 */
	public GXDLMSPppSetup() {
		super(ObjectType.PPP_SETUP);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 */
	public GXDLMSPppSetup(final String ln) {
		super(ObjectType.PPP_SETUP, ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 * @param sn
	 *            Short Name of the object.
	 */
	public GXDLMSPppSetup(final String ln, final int sn) {
		super(ObjectType.PPP_SETUP, ln, sn);
	}

	public final String getPHYReference() {
		return phyReference;
	}

	public final void setPHYReference(final String value) {
		phyReference = value;
	}

	public final GXDLMSPppSetupLcpOption[] getLCPOptions() {
		return lcpOptions;
	}

	public final void setLCPOptions(final GXDLMSPppSetupLcpOption[] value) {
		lcpOptions = value;
	}

	public final GXDLMSPppSetupIPCPOption[] getIPCPOptions() {
		return ipcpOptions;
	}

	public final void setIPCPOptions(final GXDLMSPppSetupIPCPOption[] value) {
		ipcpOptions = value;
	}

	public final Object[] getValues() {
		String str = "";
		if (userName != null) {
			str = new String(userName);
		}
		if (password != null) {
			str += " " + new String(password);
		}
		return new Object[] { getLogicalName(), getPHYReference(),
				getLCPOptions(), getIPCPOptions(), str };
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
		// PHYReference
		if (!isRead(2)) {
			attributes.addElement(new Integer(2));
		}
		// LCPOptions
		if (!isRead(3)) {
			attributes.addElement(new Integer(3));
		}
		// IPCPOptions
		if (!isRead(4)) {
			attributes.addElement(new Integer(4));
		}
		// PPPAuthentication
		if (!isRead(5)) {
			attributes.addElement(new Integer(5));
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */

	public final int getAttributeCount() {
		return 5;
	}

	/*
	 * Returns amount of methods.
	 */

	public final int getMethodCount() {
		return 0;
	}

	public final DataType getDataType(final int index) {
		if (index == 1) {
			return DataType.OCTET_STRING;
		}
		if (index == 2) {
			return DataType.OCTET_STRING;
		}
		if (index == 3) {
			return DataType.ARRAY;
		}
		if (index == 4) {
			return DataType.ARRAY;
		}
		if (index == 5) {
			return DataType.STRUCTURE;
		}
		throw new IllegalArgumentException(
				"getDataType failed. Invalid attribute index.");
	}

	/*
	 * Returns value of given attribute.
	 */

	public final Object getValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			return getLogicalName();
		}
		if (e.getIndex() == 2) {
			return phyReference;
		}
		if (e.getIndex() == 3) {
			GXByteBuffer data = new GXByteBuffer();
			data.setUInt8((byte) DataType.ARRAY.getValue());
			if (lcpOptions == null) {
				data.setUInt8(0);
			} else {
				data.setUInt8((byte) lcpOptions.length);
				for (int pos = 0; pos != lcpOptions.length; ++pos) {
					GXDLMSPppSetupLcpOption it = lcpOptions[pos];
					data.setUInt8((byte) DataType.STRUCTURE.getValue());
					data.setUInt8((byte) 3);
					GXCommon.setData(data, DataType.UINT8, it.getType());
					GXCommon.setData(data, DataType.UINT8,
							new Integer(it.getLength()));
					GXCommon.setData(data, GXCommon.getValueType(it.getData()),
							it.getData());
				}
			}
			return data.array();
		}
		if (e.getIndex() == 4) {
			GXByteBuffer data = new GXByteBuffer();
			data.setUInt8((byte) DataType.ARRAY.getValue());
			if (ipcpOptions == null) {
				data.setUInt8(0);
			} else {
				data.setUInt8((byte) ipcpOptions.length);
				for (int pos = 0; pos != ipcpOptions.length; ++pos) {
					GXDLMSPppSetupIPCPOption it = ipcpOptions[pos];
					data.setUInt8((byte) DataType.STRUCTURE.getValue());
					data.setUInt8((byte) 3);
					GXCommon.setData(data, DataType.UINT8, it.getType());
					GXCommon.setData(data, DataType.UINT8,
							new Integer(it.getLength()));
					GXCommon.setData(data, GXCommon.getValueType(it.getData()),
							it.getData());
				}
			}
			return data.array();
		} else if (e.getIndex() == 5) {
			GXByteBuffer data = new GXByteBuffer();
			data.setUInt8((byte) DataType.STRUCTURE.getValue());
			data.setUInt8(2);
			GXCommon.setData(data, DataType.OCTET_STRING, userName);
			GXCommon.setData(data, DataType.OCTET_STRING, password);
			return data.array();
		}
		e.setError(ErrorCode.READ_WRITE_DENIED);
		return null;
	}

	/*
	 * Set value of given attribute.
	 */

	public final void setValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		if (e.getIndex() == 1) {
			super.setValue(settings, e);
		} else if (e.getIndex() == 2) {
			if (e.getValue() instanceof String) {
				phyReference = e.getValue().toString();
			} else {
				phyReference = GXDLMSClient.changeType((byte[]) e.getValue(),
						DataType.OCTET_STRING).toString();
			}
		} else if (e.getIndex() == 3) {
			if (e.getValue() instanceof Object[]) {
				Object[] arr = (Object[]) e.getValue();
				lcpOptions = new GXDLMSPppSetupLcpOption[arr.length];
				for (int pos = 0; pos != arr.length; ++pos) {
					Object item = arr[pos];
					GXDLMSPppSetupLcpOption it = new GXDLMSPppSetupLcpOption();
					it.setType(GXDLMSPppSetupLcpOptionType
							.forValue(GXCommon.intValue(((Object[]) item)[0])));
					it.setLength(GXCommon.intValue(((Object[]) item)[1]));
					it.setData(((Object[]) item)[2]);
					lcpOptions[pos] = it;
				}
			} else {
				lcpOptions = new GXDLMSPppSetupLcpOption[0];
			}
		} else if (e.getIndex() == 4) {
			if (e.getValue() instanceof Object[]) {
				Object[] arr = (Object[]) e.getValue();
				ipcpOptions = new GXDLMSPppSetupIPCPOption[arr.length];
				for (int pos = 0; pos != arr.length; ++pos) {
					Object item = arr[pos];
					GXDLMSPppSetupIPCPOption it = new GXDLMSPppSetupIPCPOption();
					it.setType(GXDLMSPppSetupIPCPOptionType
							.forValue(GXCommon.intValue(((Object[]) item)[0])));
					it.setLength(GXCommon.intValue(((Object[]) item)[1]));
					it.setData(((Object[]) item)[2]);
					ipcpOptions[pos] = it;
				}
			} else {
				ipcpOptions = new GXDLMSPppSetupIPCPOption[0];
			}
		} else if (e.getIndex() == 5) {
			userName = (byte[]) ((Object[]) e.getValue())[0];
			password = (byte[]) ((Object[]) e.getValue())[1];
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}
}