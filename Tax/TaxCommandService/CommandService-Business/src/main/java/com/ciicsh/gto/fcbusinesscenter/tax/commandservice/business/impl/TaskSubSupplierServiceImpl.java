package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclarePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class TaskSubSupplierServiceImpl extends ServiceImpl<TaskSubSupplierMapper, TaskSubSupplierPO> implements TaskSubSupplierService, Serializable {

    /**
     * 当期
     */
    private static final String CURRENT_PAN = "currentPan";
    /**
     * 当期前
     */
    private static final String CURRENT_BEFORE_PAN = "currentBeforePan";
    /**
     * 当期后
     */
    private static final String CURRENT_AFTER_PAN = "currentAfterPan";

    /**
     * 查询供应商子任务
     * @param requestForTaskSubSupplier
     * @return
     */
    @Override
    public ResponseForTaskSubSupplier queryTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier) {
        ResponseForTaskSubSupplier responseForTaskSubSupplier = new ResponseForTaskSubSupplier();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubSupplierPO());
        //判断是否包含申报账户条件
        if(StrKit.isNotEmpty(requestForTaskSubSupplier.getDeclareAccount())){
            wrapper.like("declare_account",requestForTaskSubSupplier.getDeclareAccount());
        }
        //判断是否包含管理方名称条件
        if(StrKit.isNotEmpty(requestForTaskSubSupplier.getManagerName())){
            wrapper.like("manager_name",requestForTaskSubSupplier.getManagerName());
        }
        //判断是否包含个税期间条件
        if (StrKit.notBlank(requestForTaskSubSupplier.getPeriod())) {
            wrapper.andNew("period = {0} ",LocalDate.parse(requestForTaskSubSupplier.getPeriod()));
        }
        //判断是否包含供应商名称条件
        if(StrKit.isNotEmpty(requestForTaskSubSupplier.getSupportName())){
            wrapper.like("support_name",requestForTaskSubSupplier.getSupportName());
        }
        //期间类型currentPan,currentBeforePan,currentAfterPan
        if (StrKit.notBlank(requestForTaskSubSupplier.getPeriodType())) {
            String currentDateStr = DateTimeKit.format(new Date(), "YYYY-MM") + "-01";
            if (CURRENT_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period = {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_BEFORE_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period < {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            } else if (CURRENT_AFTER_PAN.equals(requestForTaskSubSupplier.getPeriodType())) {
                wrapper.andNew("period > {0}", DateTimeKit.parse(currentDateStr, DateTimeKit.NORM_DATE_PATTERN));
            }
        }
        //任务状态
        if(StrKit.notBlank(requestForTaskSubSupplier.getStatusType())){
            wrapper.andNew("status = {0}", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForTaskSubSupplier.getStatusType().toUpperCase()));
        }
        //供应商子任务ID为空
        wrapper.isNull("task_sub_supplier_id");
        //是否可用
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time",false);
        Page<TaskSubSupplierPO> page = new Page<TaskSubSupplierPO>(requestForTaskSubSupplier.getCurrentNum(),requestForTaskSubSupplier.getPageSize());
        List<TaskSubSupplierPO> taskSubSupplierPOList = baseMapper.selectPage(page, wrapper);
        responseForTaskSubSupplier.setRowList(taskSubSupplierPOList);
        responseForTaskSubSupplier.setTotalNum(page.getTotal());
        responseForTaskSubSupplier.setCurrentNum(requestForTaskSubSupplier.getCurrentNum());
        responseForTaskSubSupplier.setPageSize(requestForTaskSubSupplier.getPageSize());
        return responseForTaskSubSupplier;
    }
}
