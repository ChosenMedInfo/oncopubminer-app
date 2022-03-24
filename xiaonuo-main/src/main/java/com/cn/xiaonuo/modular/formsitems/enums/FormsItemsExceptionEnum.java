/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

XiaoNuo采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改XiaoNuo源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/xiaonuo-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/xiaonuo-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取XiaoNuo商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package com.cn.xiaonuo.modular.formsitems.enums;

import com.cn.xiaonuo.core.annotion.ExpEnumType;
import com.cn.xiaonuo.core.exception.ServiceException;
import com.cn.xiaonuo.core.exception.enums.StatusExceptionEnum;
import com.cn.xiaonuo.core.exception.enums.abs.AbstractBaseExceptionEnum;
import com.cn.xiaonuo.core.factory.ExpEnumCodeFactory;
import com.cn.xiaonuo.sys.core.consts.SysExpEnumConstant;

/**
 * @func 表单项列表
 *
 * @author Quan Xu
 * @date 2021-07-08 13:19:19
 */
@ExpEnumType(module = SysExpEnumConstant.XIAONUO_SYS_MODULE_EXP_CODE, kind = SysExpEnumConstant.SYS_POS_EXCEPTION_ENUM)
public enum FormsItemsExceptionEnum implements AbstractBaseExceptionEnum {

	/**
	 * @func 正常
	 */
	VERIFIED(0, "已审核"),

	/**
	 * @func 停用
	 */
	TO_BE_VERIFIED(1, "待审核"),

	/**
	 * @func 删除
	 */
	DELETED(2, "删除"),
	
	/**
	 * @func 数据不存在
	 */
	NOT_EXIST(3, "此数据不存在");

	private final Integer code;

	private final String message;
		FormsItemsExceptionEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return ExpEnumCodeFactory.getExpEnumCode(this.getClass(), code);
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	/**
	 * @func 获取真实code：上面的“getCode()”方法并没有返回真实的code！
	 *
	 * @author quanxu
	 * @date 2021-01-08 14:40:00
	 */
	public Integer getRealCode() {
		return code;
	}
	
	/**
	 * @func 检查请求参数的状态是否正确
	 *
	 * @author quanxu
	 * @date 2021-01-07 14:40:00
	 */
	public static void validateStatus(Integer code) {
		if (code == null) {
			throw new ServiceException(StatusExceptionEnum.REQUEST_EMPTY);
		}
		if (VERIFIED.getRealCode().equals(code) || TO_BE_VERIFIED.getRealCode().equals(code)) {
			return;
		}
		throw new ServiceException(StatusExceptionEnum.NOT_WRITE_STATUS);
	}

}
