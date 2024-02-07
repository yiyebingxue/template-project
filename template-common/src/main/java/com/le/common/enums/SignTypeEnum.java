package com.le.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 签名类型
 *
 * @author Bruce Lu
 */
@Getter
public enum SignTypeEnum {

	/**
	 * 无
	 */
	EMPTY(0),
	/**
	 * md5
	 */
	MD5(1),
	/**
	 * rsa
	 */
	RSA(2),
	;
	private final Integer value;

	SignTypeEnum(Integer value) {
		this.value = value;
	}

	public static SignTypeEnum getByValue(String value) {
		int intValue = NumberUtils.toInt(value);
		if (intValue == 0) {
			return null;
		}
		return getByValue(intValue);
	}

	public static SignTypeEnum getByValue(Integer value) {
		for (SignTypeEnum signTypeEnum : SignTypeEnum.values()) {
			if (value.equals(signTypeEnum.getValue())) {
				return signTypeEnum;
			}
		}
		return EMPTY;
	}
}
