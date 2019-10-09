package com.fengdis.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fengdis.common.component.rpc.redis.RedisUtils;
import com.fengdis.system.domain.SysUser;
import com.fengdis.system.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fengdis.common.core.controller.BaseController;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.utils.ServletUtils;
import com.fengdis.common.utils.StringUtils;

/**
 * 登录验证
 * 
 * @author fengdis
 */
@Controller
public class SysLoginController extends BaseController
{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ISysUserService iSysUserService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            ResponseEntity<String> ajaxResult = AjaxResult.error("未登录或登录超时。请重新登录");
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}"/*JSON.marshal(ajaxResult)*/);
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> ajaxLogin(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            SysUser sysUser = iSysUserService.selectUserByLoginName(username);
            redisUtils.set(username,sysUser);
            return AjaxResult.success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}
