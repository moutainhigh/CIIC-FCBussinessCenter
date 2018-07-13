package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.ConfigTaskMsgDTO;
import com.ciicsh.gto.salarymanagement.entity.po.CmyFcConfigureTaskPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.GroupExampleChangeMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.GroupExampleChangeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @createTime 2018-07-11 16:48
 * @description 薪资组实例变更任务单实现类
 */
@Service
public class GroupExampleChangeServiceImpl implements GroupExampleChangeService {

    @Autowired
    private GroupExampleChangeMapper groupExampleChangeMapper;

    private static final Logger log = LoggerFactory.getLogger(GroupExampleChangeServiceImpl.class);

    @Override
    public PageInfo<CmyFcConfigureTaskPO> selectPrGroupExampleList(CmyFcConfigureTaskPO cmyFcConfigureTaskPO,
                                                                   Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CmyFcConfigureTaskPO> resultList = groupExampleChangeMapper.selectPrGroupExampleList(cmyFcConfigureTaskPO);

        return new PageInfo<>(resultList);
    }

    @Override
    public void filterToSave(ConfigTaskMsgDTO taskMsgDTO) {
        if ("1".equals(taskMsgDTO.getTaskType())){
            log.info("Received from message:" + taskMsgDTO.toString());
            CmyFcConfigureTaskPO cmyFcConfigureTaskPO = new CmyFcConfigureTaskPO();
            cmyFcConfigureTaskPO.setCmyFcConfigureTaskId(taskMsgDTO.getCmyFcConfigureTaskId());
            cmyFcConfigureTaskPO.setStatus(1);
            cmyFcConfigureTaskPO.setType(Integer.valueOf(taskMsgDTO.getTaskType()));
            cmyFcConfigureTaskPO.setManagementId(taskMsgDTO.getManagementId());
            cmyFcConfigureTaskPO.setTargetId(taskMsgDTO.getTargetId());
            cmyFcConfigureTaskPO.setCreatedBy("sys");
            cmyFcConfigureTaskPO.setModifiedBy("sys");
            cmyFcConfigureTaskPO.setRemark(taskMsgDTO.getRemark());
            log.info("Information after conversion:" + cmyFcConfigureTaskPO.toString());
            groupExampleChangeMapper.inserTcmyFcConfigureTask(cmyFcConfigureTaskPO);
        }
    }
}
