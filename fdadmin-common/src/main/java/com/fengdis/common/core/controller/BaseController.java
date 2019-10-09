package com.fengdis.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fengdis.common.core.domain.AjaxResult;
import com.fengdis.common.core.page.PageDomain;
import com.fengdis.common.core.page.TableDataInfo;
import com.fengdis.common.core.page.TableSupport;
import com.fengdis.common.utils.DateUtils;
import com.fengdis.common.utils.ServletUtils;
import com.fengdis.common.utils.StringUtils;
import com.fengdis.common.utils.sql.SqlUtil;

/**
 * web层通用数据处理
 * 
 * @author fengdis
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    protected Pageable buildPageable()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            pageDomain.setOrderByColumn(orderBy);
        }

        String orderString = pageDomain.getIsAsc();
        String sortString = pageDomain.getOrderByColumn();
        String sortArr[] = new String[] {};
        String orderArr[] = new String[] {};

        if (!StringUtils.isEmpty(sortString)) {
            sortArr = sortString.split(",");
        }
        if (!StringUtils.isEmpty(orderString)) {
            orderArr = orderString.split(",");
        }
        if (sortArr.length == 0) {
            return PageRequest.of(pageDomain.getPageNum() - 1, pageDomain.getPageSize());
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (int i = 0; i < sortArr.length; i++) {
            Sort.Order order = new Sort.Order(Sort.Direction.fromString(orderArr[i].toUpperCase()), sortArr[i]);
            orders.add(order);
        }

        return PageRequest.of(pageDomain.getPageNum() - 1, pageDomain.getPageSize(), new Sort(orders));
    }


    /**
     * 获取request
     */
    public HttpServletRequest getRequest()
    {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse()
    {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession()
    {
        return getRequest().getSession();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected ResponseEntity<String> toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected ResponseEntity<String> toAjax(boolean result)
    {
        return result ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }
}
