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

//
//--------------------------------------------------------------------------
//Gurux Ltd
//
//
//
package gurux.dlms.objects;

import java.util.Vector;

import gurux.dlms.GXDLMSSettings;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;
import gurux.dlms.objects.enums.BaudRate;
import gurux.dlms.objects.enums.IecTwistedPairSetupMode;

public class GXDLMSIecTwistedPairSetup extends GXDLMSObject
		implements IGXDLMSBase {

	/**
	 * Working mode.
	 */
	private IecTwistedPairSetupMode mode;

	/**
	 * Communication speed.
	 */
	private BaudRate speed;

	/**
	 * List of Primary Station Addresses.
	 */
	private byte[] primaryAddresses;

	/**
	 * List of the TAB(i) for which the real equipment has been programmed in
	 * the case of forgotten station call.
	 */
	private byte[] tabis;

	/**
	 * Constructor.
	 */
	public GXDLMSIecTwistedPairSetup() {
		super(ObjectType.IEC_TWISTED_PAIR_SETUP);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 */
	public GXDLMSIecTwistedPairSetup(final String ln) {
		super(ObjectType.IEC_TWISTED_PAIR_SETUP, ln, 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 * @param sn
	 *            Short Name of the object.
	 */
	public GXDLMSIecTwistedPairSetup(final String ln, final int sn) {
		super(ObjectType.IEC_TWISTED_PAIR_SETUP, ln, sn);
	}

	/**
	 * Gets Working mode.
	 * 
	 * @return Working mode.
	 */
	public final IecTwistedPairSetupMode getMode() {
		return mode;
	}

	/**
	 * Sets working mode.
	 * 
	 * @param value
	 *            Working mode.
	 */
	public final void setMode(final IecTwistedPairSetupMode value) {
		mode = value;
	}

	/**
	 * Gets communication speed.
	 * 
	 * @return Communication speed.
	 */
	public final BaudRate getSpeed() {
		return speed;
	}

	/**
	 * Gets communication speed.
	 * 
	 * @param value
	 *            Communication speed.
	 */
	public final void setSpeed(final BaudRate value) {
		speed = value;
	}

	/**
	 * Gets list of Primary Station Addresses.
	 * 
	 * @return List of Primary Station Addresses.
	 */
	public final byte[] getPrimaryAddresses() {
		return primaryAddresses;
	}

	/**
	 * Sets list of Primary Station Addresses.
	 * 
	 * @param value
	 *            List of Primary Station Addresses.
	 */
	public final void setPrimaryAddresses(final byte[] value) {
		primaryAddresses = value;
	}

	/**
	 * Gets list of the TABis.
	 * 
	 * @return List of the TABis.
	 */
	public final byte[] getTabis() {
		return tabis;
	}

	/**
	 * Sets list of the TABis.
	 * 
	 * @param value
	 *            List of the TABis.
	 */
	public final void setTabis(final byte[] value) {
		tabis = value;
	}

	public final Object[] getValues() {
		return new Object[] { getLogicalName(), getMode(), getSpeed(),
				getPrimaryAddresses(), getTabis() };
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
		// Mode
		if (canRead(2)) {
			attributes.addElement(new Integer(2));
		}
		// Speed
		if (canRead(3)) {
			attributes.addElement(new Integer(3));
		}
		// PrimaryAddresses
		if (canRead(4)) {
			attributes.addElement(new Integer(4));
		}
		// Tabis
		if (canRead(5)) {
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
			return DataType.ENUM;
		}
		if (index == 3) {
			return DataType.ENUM;
		}
		if (index == 4) {
			return DataType.ARRAY;
		}
		if (index == 5) {
			return DataType.ARRAY;
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
			setMode(IecTwistedPairSetupMode
					.forValue(GXCommon.intValue(e.getValue())));
		} else if (e.getIndex() == 3) {
			setSpeed(BaudRate.forValue(GXCommon.intValue(e.getValue())));
		} else if (e.getIndex() == 4) {
			primaryAddresses = (byte[]) e.getValue();
		} else if (e.getIndex() == 5) {
			tabis = (byte[]) e.getValue();
		} else {
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}
}