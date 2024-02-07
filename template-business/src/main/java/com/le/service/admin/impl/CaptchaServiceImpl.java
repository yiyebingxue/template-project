/**
 * Copyright ( @程序员小强 ） All Rights Reserved.
 * 博客地址:https://blog.csdn.net/qq_38011415
 */
package com.le.service.admin.impl;

import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.le.common.constant.CacheConstants;
import com.le.common.constant.SsoConstants;
import com.le.common.exception.BusinessException;
import com.le.common.utils.UUIDUtil;
import com.le.framework.config.property.SysConfigProperty;
import com.le.framework.redis.RedisCache;
import com.le.model.vo.captcha.CaptchaVO;
import com.le.service.admin.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * 验证码接口
 *
 * @author Bruce Lu
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Resource
	private RedisCache redisCache;

	@Resource(name = "captchaProducer")
	private Producer captchaProducer;

	@Resource(name = "captchaProducerMath")
	private Producer captchaProducerMath;

	@Resource
	private SysConfigProperty sysConfigProperty;


	/**
	 * 生成验证码
	 *
	 * @param response
	 */
	public CaptchaVO genCode(HttpServletResponse response) {
		//生成随机请求ID
		String requestId = UUIDUtil.getUUID();
		String capStr = null;
		String code = null;
		BufferedImage image = null;

		// 生成验证码
		if (SsoConstants.CAPTCHA_TYPE_MATH.equals(sysConfigProperty.getCaptchaType())) {
			String capText = captchaProducerMath.createText();
			capStr = capText.substring(0, capText.lastIndexOf("@"));
			code = capText.substring(capText.lastIndexOf("@") + 1);
			image = captchaProducerMath.createImage(capStr);
		} else if (SsoConstants.CAPTCHA_TYPE_CHAR.equals(sysConfigProperty.getCaptchaType())) {
			capStr = code = captchaProducer.createText();
			image = captchaProducer.createImage(capStr);
		} else {
			throw new BusinessException("不支持的验证码类型");
		}


		//验证码5分钟内有效
		String cacheKey = CacheConstants.getCaptchaCodeKey(requestId);
		redisCache.set(cacheKey, code, CacheConstants.CAPTCHA_EXPIRE, TimeUnit.MINUTES);

		//转换流
		FastByteArrayOutputStream os = new FastByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", os);
		} catch (IOException e) {
			throw new BusinessException("验证码生成错误!");
		}

		return new CaptchaVO(requestId, Base64.encode(os.toByteArray()));
	}
}
