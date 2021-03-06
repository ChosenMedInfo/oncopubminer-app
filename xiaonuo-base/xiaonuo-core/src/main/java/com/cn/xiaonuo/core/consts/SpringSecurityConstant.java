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
package com.cn.xiaonuo.core.consts;

/**
 * SpringSecurity相关常量
 *
 * @author xuyuxiang
 * @date 2020/3/18 17:49
 */
public interface SpringSecurityConstant {

    /**
     * 放开权限校验的接口
     */
    String[] NONE_SECURITY_URL_PATTERNS = {

            //前端的
            "/assets/**",
            "/favicon.ico",

            //swagger相关的
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/configuration/ui",
            "/configuration/security",

            //flowable工作流相关的
            "/designer/**",
            "/app/rest/**",

            //后端的
            "/",
            "/403",
            "/404",
            "/500",
            "/login",
            "/loginPost", // 2021-12-13！
            "/logout",
            "/getCaptcha",
            "/oauth/**",
            "/global/sessionError",
            "/error",
            "/global/error",

            //文件的
            "/sysFileInfo/upload",
            "/sysFileInfo/download",
            "/sysFileInfo/preview",

            //druid的
            "/druid/**",

            //支付相关的
            "/aliPay/**",
            "/wxPay/**",

            //获取租户列表
            "/getTenantOpen",
            "/tenantInfo/listTenants",
            
            // LitReviewer:
            "/litReviewer",
            "/getLoginUser",
            "/sysUser/currentLoginUserInfo", // 2021-10-13：获取登录用户信息！
            "/sysDictType/tree",
            "/register",
            "/registerPost", // 2021-12-13！
            //"/sysUser/addMember",
            //"/sysUser/editMember",
            //"/sysUser/listMember", // 2021-11-19：注释掉，这个需要登录才能操作，不过相应的Permission要求去掉！
            //"/sysUser/delMember", // 2021-11-19：删除成员用户！（注释掉，这个需要登录才能操作，不过相应的Permission要求去掉）
            "/forms/*",
            "/groups/*",
            "/libraries/list", // 2021-10-13：单用户阅读模式，需要获取文库列表！
            "/forms/detail", // 2021-10-13：单用户阅读模式，需要获取数据收集表单信息！
            "/formsItems/list", // 2021-10-13：单用户阅读模式，需要获取数据收集表单之表项信息！
            "/formsItemsData/add", // 2021-10-13：单用户阅读模式，需要有数据提交权限！
            "/formsItemsData/exportData", // 2021-10-13：单用户阅读模式，导出提交的数据！
            "/formsItemsData/updateCivicData", // 2021-10-18：CIViC数据更新链接！
            "/librariesPublications/updatePublicationDetail", // 2021-11-19：文献基本信息更新链接！
            "/errorReport/add", // 2021-11-08：错误报告！
            "/feedback/add", // 2021-11-15：用户反馈信息！
            "/stat/index", // 2021-11-15：首页数据统计！
            "/getVersion", // 2021-11-22：获取版本信息！
            "/formsItemsData/listData", // 2021-11-30：获取全部数据，用于在表格中展示！
            "/demoGroupAdmin", // 2021-12-29：示例账号登录！
            "/demoGroupMemberA", // 2021-12-29：示例账号登录！
            "/demoGroupMemberB", // 2021-12-29：示例账号登录！
            "/demoGroupMemberC", // 2021-12-29：示例账号登录！
            "/opmUser", // 2022-03-17：链接登录！
            "/opmUserLogin", // 2022-03-17：链接登录！
            "/lr*"
    };

}
