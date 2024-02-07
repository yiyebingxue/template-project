package com.le.common.utils;

import com.google.common.base.CaseFormat;
import com.le.common.enums.exception.ApiExceptionEnum;
import com.le.common.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;

/**
 * 业务参数校验
 *
 * @author Bruce Lu
 */
public class ValidateUtils {

	private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	private ValidateUtils() {
	}

	public static <T> void validate(T object) {
		//执行验证
		Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);
		//如果有验证信息，则取出包装成异常返回
		ConstraintViolation<T> constraintViolation = getFirst(constraintViolations, null);
		if (constraintViolation != null) {
			throw new BusinessException(ApiExceptionEnum.INVALID_PARAM,
					CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, constraintViolation.getPropertyPath().toString()),
					constraintViolation.getMessage());
		}
	}

}


