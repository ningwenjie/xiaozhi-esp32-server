package xiaozhi.modules.agent.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import xiaozhi.common.dao.BaseDao;
import xiaozhi.modules.agent.entity.LearningRecordEntity;

@Mapper
public interface LearningRecordDao extends BaseDao<LearningRecordEntity> {
    
    /**
     * 根据儿童姓名查询学习记录
     * 
     * @param childName 儿童姓名
     * @return 学习记录列表
     */
    List<LearningRecordEntity> selectByChildName(@Param("childName") String childName);
    
    /**
     * 根据场景ID查询学习记录
     * 
     * @param scenarioId 场景ID
     * @return 学习记录列表
     */
    List<LearningRecordEntity> selectByScenarioId(@Param("scenarioId") String scenarioId);
    
    /**
     * 根据会话ID查询学习记录
     * 
     * @param sessionId 会话ID
     * @return 学习记录列表
     */
    List<LearningRecordEntity> selectBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * 根据儿童姓名和日期范围查询学习记录
     * 
     * @param childName 儿童姓名
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 学习记录列表
     */
    List<LearningRecordEntity> selectByChildNameAndDateRange(@Param("childName") String childName, 
                                                           @Param("startDate") Date startDate, 
                                                           @Param("endDate") Date endDate);
    
    /**
     * 根据场景ID和儿童姓名查询学习记录
     * 
     * @param scenarioId 场景ID
     * @param childName 儿童姓名
     * @return 学习记录列表
     */
    List<LearningRecordEntity> selectByScenarioIdAndChildName(@Param("scenarioId") String scenarioId, 
                                                             @Param("childName") String childName);
    
    /**
     * 统计儿童的学习成功率
     * 
     * @param childName 儿童姓名
     * @param scenarioId 场景ID（可选）
     * @return 成功率
     */
    Double getSuccessRate(@Param("childName") String childName, @Param("scenarioId") String scenarioId);
    
    /**
     * 统计儿童的学习次数
     * 
     * @param childName 儿童姓名
     * @param scenarioId 场景ID（可选）
     * @return 学习次数
     */
    Integer getLearningCount(@Param("childName") String childName, @Param("scenarioId") String scenarioId);
} 