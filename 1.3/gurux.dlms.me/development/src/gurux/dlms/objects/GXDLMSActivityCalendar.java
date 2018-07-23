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
import gurux.dlms.GXDateTime;
import gurux.dlms.GXTime;
import gurux.dlms.ValueEventArgs;
import gurux.dlms.enums.DataType;
import gurux.dlms.enums.ErrorCode;
import gurux.dlms.enums.ObjectType;
import gurux.dlms.internal.GXCommon;

public class GXDLMSActivityCalendar extends GXDLMSObject
		implements IGXDLMSBase {
	private String calendarNameActive;
	private String calendarNamePassive;
	private GXDLMSSeasonProfile[] seasonProfileActive;
	private GXDLMSWeekProfile[] weekProfileTableActive;
	private GXDLMSDayProfile[] dayProfileTableActive;
	private GXDLMSSeasonProfile[] seasonProfilePassive;
	private GXDLMSDayProfile[] dayProfileTablePassive;
	private GXDLMSWeekProfile[] weekProfileTablePassive;
	private GXDateTime time;

	/**
	 * Constructor.
	 */
	public GXDLMSActivityCalendar() {
		this("0.0.13.0.0.255");
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 */
	public GXDLMSActivityCalendar(final String ln) {
		this(ln, (short) 0);
	}

	/**
	 * Constructor.
	 * 
	 * @param ln
	 *            Logical Name of the object.
	 * @param sn
	 *            Short Name of the object.
	 */
	public GXDLMSActivityCalendar(final String ln, final int sn) {
		super(ObjectType.ACTIVITY_CALENDAR, ln, sn);
	}

	public final String getCalendarNameActive() {
		return calendarNameActive;
	}

	public final void setCalendarNameActive(final String value) {
		calendarNameActive = value;
	}

	public final GXDLMSSeasonProfile[] getSeasonProfileActive() {
		return seasonProfileActive;
	}

	public final void setSeasonProfileActive(
			final GXDLMSSeasonProfile[] value) {
		seasonProfileActive = value;
	}

	public final GXDLMSWeekProfile[] getWeekProfileTableActive() {
		return weekProfileTableActive;
	}

	public final void setWeekProfileTableActive(
			final GXDLMSWeekProfile[] value) {
		weekProfileTableActive = value;
	}

	public final GXDLMSDayProfile[] getDayProfileTableActive() {
		return dayProfileTableActive;
	}

	public final void setDayProfileTableActive(final GXDLMSDayProfile[] value) {
		dayProfileTableActive = value;
	}

	public final String getCalendarNamePassive() {
		return calendarNamePassive;
	}

	public final void setCalendarNamePassive(final String value) {
		calendarNamePassive = value;
	}

	public final GXDLMSSeasonProfile[] getSeasonProfilePassive() {
		return seasonProfilePassive;
	}

	public final void setSeasonProfilePassive(
			final GXDLMSSeasonProfile[] value) {
		seasonProfilePassive = value;
	}

	public final GXDLMSWeekProfile[] getWeekProfileTablePassive() {
		return weekProfileTablePassive;
	}

	public final void setWeekProfileTablePassive(
			final GXDLMSWeekProfile[] value) {
		weekProfileTablePassive = value;
	}

	public final GXDLMSDayProfile[] getDayProfileTablePassive() {
		return dayProfileTablePassive;
	}

	public final void setDayProfileTablePassive(
			final GXDLMSDayProfile[] value) {
		dayProfileTablePassive = value;
	}

	public final GXDateTime getTime() {
		return time;
	}

	public final void setTime(final GXDateTime value) {
		time = value;
	}

	public final Object[] getValues() {
		return new Object[] { getLogicalName(), getCalendarNameActive(),
				getSeasonProfileActive(), getWeekProfileTableActive(),
				getDayProfileTableActive(), getCalendarNamePassive(),
				getSeasonProfilePassive(), getWeekProfileTablePassive(),
				getDayProfileTablePassive(), getTime() };
	}

	/*
	 * Returns collection of attributes to read. If attribute is static and
	 * already read or device is returned HW error it is not returned.
	 */

	public final int[] getAttributeIndexToRead() {
		final Vector attributes = new Vector();
		// LN is static and read only once.
		if (getLogicalName() == null || getLogicalName().compareTo("") == 0) {
			attributes.addElement(new Integer(1));
		}
		// CalendarNameActive
		if (canRead(2)) {
			attributes.addElement(new Integer(2));
		}
		// SeasonProfileActive
		if (canRead(3)) {
			attributes.addElement(new Integer(3));
		}

		// WeekProfileTableActive
		if (canRead(4)) {
			attributes.addElement(new Integer(4));
		}
		// DayProfileTableActive
		if (canRead(5)) {
			attributes.addElement(new Integer(5));
		}
		// CalendarNamePassive
		if (canRead(6)) {
			attributes.addElement(new Integer(6));
		}
		// SeasonProfilePassive
		if (canRead(7)) {
			attributes.addElement(new Integer(7));
		}
		// WeekProfileTablePassive
		if (canRead(8)) {
			attributes.addElement(new Integer(8));
		}
		// DayProfileTablePassive
		if (canRead(9)) {
			attributes.addElement(new Integer(9));
		}
		// Time.
		if (canRead(10)) {
			attributes.addElement(new Integer(10));
		}
		return GXDLMSObjectHelpers.toIntArray(attributes);
	}

	/*
	 * Returns amount of attributes.
	 */

	public final int getAttributeCount() {
		return 10;
	}

	/*
	 * Returns amount of methods.
	 */

	public final int getMethodCount() {
		return 1;
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
			return DataType.ARRAY;
		}
		if (index == 6) {
			return DataType.OCTET_STRING;
		}
		if (index == 7) {
			return DataType.ARRAY;
		}
		if (index == 8) {
			return DataType.ARRAY;
		}
		if (index == 9) {
			return DataType.ARRAY;
		}
		if (index == 10) {
			return DataType.OCTET_STRING;
		}
		throw new IllegalArgumentException(
				"getDataType failed. Invalid attribute index.");
	}

	static Object getSeasonProfile(final GXDLMSSeasonProfile[] target) {
		GXByteBuffer data = new GXByteBuffer();
		data.setUInt8(DataType.ARRAY.getValue());
		if (target == null) {
			// Add count
			GXCommon.setObjectCount(0, data);
		} else {
			int cnt = target.length;
			// Add count
			GXCommon.setObjectCount(cnt, data);
			for (int pos = 0; pos != target.length; ++pos) {
				GXDLMSSeasonProfile it = target[pos];
				data.setUInt8(DataType.STRUCTURE.getValue());
				data.setUInt8(3);
				GXCommon.setData(data, DataType.OCTET_STRING,
						GXCommon.getBytes(it.getName()));
				GXCommon.setData(data, DataType.OCTET_STRING, it.getStart());
				GXCommon.setData(data, DataType.OCTET_STRING,
						GXCommon.getBytes(it.getWeekName()));
			}
		}
		return data.array();
	}

	static Object getWeekProfileTable(final GXDLMSWeekProfile[] target) {
		GXByteBuffer data = new GXByteBuffer();
		data.setUInt8(DataType.ARRAY.getValue());
		if (target == null) {
			// Add count
			GXCommon.setObjectCount(0, data);
		} else {
			// Add count
			GXCommon.setObjectCount(target.length, data);
			for (int pos = 0; pos != target.length; ++pos) {
				GXDLMSWeekProfile it = target[pos];
				data.setUInt8(DataType.ARRAY.getValue());
				data.setUInt8(8);
				GXCommon.setData(data, DataType.OCTET_STRING,
						GXCommon.getBytes(it.getName()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getMonday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getTuesday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getWednesday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getThursday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getFriday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getSaturday()));
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getSunday()));
			}
		}
		return data.array();
	}

	static Object getDayProfileTable(final GXDLMSDayProfile[] target) {
		GXByteBuffer data = new GXByteBuffer();
		data.setUInt8(DataType.ARRAY.getValue());
		if (target == null) {
			// Add count
			GXCommon.setObjectCount(0, data);
		} else {
			// Add count
			GXCommon.setObjectCount(target.length, data);
			for (int pos = 0; pos != target.length; ++pos) {
				GXDLMSDayProfile it = target[pos];
				data.setUInt8(DataType.STRUCTURE.getValue());
				data.setUInt8(2);
				GXCommon.setData(data, DataType.UINT8,
						new Integer(it.getDayId()));
				data.setUInt8(DataType.ARRAY.getValue());
				// Add count
				GXCommon.setObjectCount(it.getDaySchedules().length, data);
				for (int pos2 = 0; pos2 != it
						.getDaySchedules().length; ++pos2) {
					GXDLMSDayProfileAction action = it.getDaySchedules()[pos2];
					data.setUInt8(DataType.STRUCTURE.getValue());
					data.setUInt8(3);
					GXCommon.setData(data, DataType.OCTET_STRING,
							action.getStartTime());
					GXCommon.setData(data, DataType.OCTET_STRING,
							action.getScriptLogicalName());
					GXCommon.setData(data, DataType.UINT16,
							new Integer(action.getScriptSelector()));
				}
			}
		}
		return data.array();
	}

	/*
	 * Returns value of given attribute.
	 */

	public final Object getValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			return getLogicalName();
		case 2:
			return GXDLMSClient.changeType(
					GXCommon.getBytes(getCalendarNameActive()),
					DataType.OCTET_STRING);
		case 3:
			return getSeasonProfile(seasonProfileActive);
		case 4:
			return getWeekProfileTable(weekProfileTableActive);
		case 5:
			return getDayProfileTable(dayProfileTableActive);
		case 6:
			return GXDLMSClient.changeType(
					GXCommon.getBytes(getCalendarNamePassive()),
					DataType.OCTET_STRING);
		case 7:
			return getSeasonProfile(seasonProfilePassive);
		case 8:
			return getWeekProfileTable(weekProfileTablePassive);
		case 9:
			return getDayProfileTable(dayProfileTablePassive);
		case 10:
			return getTime();
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
			return null;
		}
	}

	static GXDLMSSeasonProfile[] getSeasonProfile(final Object value) {
		final Vector items = new Vector();
		if (value != null) {
			for (int pos = 0; pos != ((Object[]) value).length; ++pos) {
				Object item = ((Object[]) value)[pos];
				final GXDLMSSeasonProfile it = new GXDLMSSeasonProfile();
				it.setName(
						GXDLMSClient.changeType((byte[]) ((Object[]) item)[0],
								DataType.STRING).toString());
				it.setStart((GXDateTime) GXDLMSClient.changeType(
						(byte[]) ((Object[]) item)[1], DataType.DATE));
				it.setWeekName(
						GXDLMSClient.changeType((byte[]) ((Object[]) item)[2],
								DataType.STRING).toString());
				items.addElement(it);
			}
		}
		GXDLMSSeasonProfile[] tmp2 = new GXDLMSSeasonProfile[items.size()];
		items.copyInto(tmp2);
		return tmp2;
	}

	static GXDLMSWeekProfile[] getWeekProfileTable(final Object value) {
		final Vector items = new Vector();
		if (value != null) {
			for (int pos = 0; pos != ((Object[]) value).length; ++pos) {
				Object item = ((Object[]) value)[pos];
				Object[] arr = ((Object[]) item);
				final GXDLMSWeekProfile it = new GXDLMSWeekProfile();
				it.setName(GXDLMSClient
						.changeType((byte[]) arr[0], DataType.STRING)
						.toString());
				it.setMonday(GXCommon.intValue(arr[1]));
				it.setTuesday(GXCommon.intValue(arr[2]));
				it.setWednesday(GXCommon.intValue(arr[3]));
				it.setThursday(GXCommon.intValue(arr[4]));
				it.setFriday(GXCommon.intValue(arr[5]));
				it.setSaturday(GXCommon.intValue(arr[6]));
				it.setSunday(GXCommon.intValue(arr[7]));
				items.addElement(it);
			}
		}
		GXDLMSWeekProfile[] tmp2 = new GXDLMSWeekProfile[items.size()];
		items.copyInto(tmp2);
		return tmp2;
	}

	static GXDLMSDayProfile[] getDayProfileTable(final Object value) {
		final Vector items = new Vector();
		if (value != null) {
			for (int pos = 0; pos != ((Object[]) value).length; ++pos) {
				Object item = ((Object[]) value)[pos];
				final GXDLMSDayProfile it = new GXDLMSDayProfile();
				it.setDayId(GXCommon.intValue(((Object[]) item)[0]));
				final Vector actions = new Vector();
				for (int pos2 = 0; pos2 != ((Object[]) ((Object[]) item)[1]).length; ++pos2) {
					Object it2 = ((Object[]) ((Object[]) item)[1])[pos2];
					final GXDLMSDayProfileAction ac = new GXDLMSDayProfileAction();
					ac.setStartTime((GXTime) GXDLMSClient.changeType(
							(byte[]) ((Object[]) it2)[0], DataType.TIME));
					ac.setScriptLogicalName(GXDLMSClient
							.changeType((byte[]) ((Object[]) it2)[1],
									DataType.OCTET_STRING)
							.toString());
					ac.setScriptSelector(
							GXCommon.intValue(((Object[]) it2)[2]));
					actions.addElement(ac);
				}
				GXDLMSDayProfileAction[] tmp2 = new GXDLMSDayProfileAction[actions
						.size()];
				items.copyInto(tmp2);
				it.setDaySchedules(tmp2);
				items.addElement(it);
			}
		}
		GXDLMSDayProfile[] tmp2 = new GXDLMSDayProfile[items.size()];
		items.copyInto(tmp2);
		return tmp2;
	}

	/*
	 * Set value of given attribute.
	 */

	public final void setValue(final GXDLMSSettings settings,
			final ValueEventArgs e) {
		switch (e.getIndex()) {
		case 1:
			super.setValue(settings, e);
			break;
		case 2:
			setCalendarNameActive(GXDLMSClient
					.changeType((byte[]) e.getValue(), DataType.STRING)
					.toString());
			break;
		case 3:
			setSeasonProfileActive(getSeasonProfile(e.getValue()));
			break;
		case 4:
			setWeekProfileTableActive(getWeekProfileTable(e.getValue()));
			break;
		case 5:
			setDayProfileTableActive(getDayProfileTable(e.getValue()));
			break;
		case 6:
			setCalendarNamePassive(GXDLMSClient
					.changeType((byte[]) e.getValue(), DataType.STRING)
					.toString());
			break;
		case 7:
			setSeasonProfilePassive(getSeasonProfile(e.getValue()));
			break;
		case 8:
			setWeekProfileTablePassive(getWeekProfileTable(e.getValue()));
			break;
		case 9:
			setDayProfileTablePassive(getDayProfileTable(e.getValue()));
			break;
		case 10:
			if (e.getValue() instanceof GXDateTime) {
				setTime((GXDateTime) e.getValue());
			} else {
				setTime((GXDateTime) GXDLMSClient
						.changeType((byte[]) e.getValue(), DataType.DATETIME));
			}
			break;
		default:
			e.setError(ErrorCode.READ_WRITE_DENIED);
		}
	}
}