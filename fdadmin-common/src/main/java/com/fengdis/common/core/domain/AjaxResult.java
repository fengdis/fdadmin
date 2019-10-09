package com.fengdis.common.core.domain;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.fengdis.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * 操作消息提醒
 * 
 * @author fengdis
 */
public class AjaxResult extends HashMap<String, Object> {


    static Logger logger = LoggerFactory.getLogger(AjaxResult.class);


    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    public static final String CONTENT_TYPE_FOR_XML = "application/xml";
    public static final String CONTENT_TYPE_FOR_JSON = "application/json";

    /**
     * 状态类型
     */
    public enum Type {
        /** 成功 */
        SUCCESS(200),
        /** 警告 */
        WARN(301),
        /** 错误 */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param type 状态类型
     * @param msg 返回内容
     */
    public AjaxResult(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     * 
     * @param type 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public AjaxResult(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    public static ResponseEntity<String> jsonResult(String result) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.set("Content-Type", CONTENT_TYPE_FOR_JSON + ";charset=UTF-8");
        return new ResponseEntity<String>(result, resHeaders, HttpStatus.OK);
    }

    /**
     * 返回成功消息
     * 
     * @return 成功消息
     */
    public static ResponseEntity<String> success() {
        Map<String,Object> map = new AjaxResult(Type.SUCCESS, "操作成功", null);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static ResponseEntity<String> success(Object data) {
        Map<String,Object> map = new AjaxResult(Type.SUCCESS, "操作成功", data);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResponseEntity<String> success(String msg) {
        Map<String,Object> map = new AjaxResult(Type.SUCCESS, msg, null);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResponseEntity<String> success(String msg, Object data) {
        Map<String,Object> map = new AjaxResult(Type.SUCCESS, msg, data);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回警告消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResponseEntity<String> warn(String msg) {
        Map<String, Object> map = new AjaxResult(Type.WARN, msg, null);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回警告消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResponseEntity<String> warn(String msg, Object data) {
        Map<String, Object> map = new AjaxResult(Type.WARN, msg, data);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回错误消息
     * 
     * @return
     */
    public static ResponseEntity<String> error() {
        Map<String, Object> map = new AjaxResult(Type.ERROR, "操作失败", null);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResponseEntity<String> error(String msg) {
        Map<String, Object> map = new AjaxResult(Type.ERROR, msg, null);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResponseEntity<String> error(String msg, Object data) {
        Map<String, Object> map = new AjaxResult(Type.ERROR, msg, data);
        return jsonResult(JSONObject.toJSONString(map));
    }

    /**
     * AJAX文本内容返回<br>
     *
     * @param response
     * @param text
     */
    public static void ajax4TextResult(HttpServletResponse response, String text) {
        ajax4Result(response, text, null);
    }

    /**
     * AJAX返回XML<br>
     *
     * @param response
     * @param text
     */
    public static void ajax4XMLResult(HttpServletResponse response, String text) {
        ajax4Result(response, text, CONTENT_TYPE_FOR_XML);
    }

    /**
     * AJAX返回 <br>
     *
     * @param response
     * @param text
     * @param type
     */
    private static void ajax4Result(HttpServletResponse response, String text, String type) {
        PrintWriter printWriter = null;
        try {
            if (null != type)
                response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            printWriter = response.getWriter();
            printWriter.print(text);
        } catch (IOException e) {
            logger.error(String.format("AJAX返回失败:%s,返回类型:%s", text, type),e);
        } finally {
            if (null != printWriter) {
                printWriter.close();
            }
        }
    }


}
