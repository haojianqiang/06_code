package com.gpdi.schedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import pub.dao.GeneralDao;
import pub.functions.StrFuncs;


/**
 * 任务调度父类
 * <p>
 * 子类继承并重写execute()方法，实现业务逻辑
 * 调度周期 cron 在库表 sys_schedule_config 配置，其中 task_class 字段值为子类类名
 *
 * @author zzj
 * @date 2019/8/14 0014
 */
@Component
@Slf4j
public class Task implements SchedulingConfigurer {
    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // 设置线程池
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
        // 添加定时任务
        scheduledTaskRegistrar.addTriggerTask(
                // 执行任务
                () -> execute(),
                // 执行周期
                triggerContext -> {
                    // 从数据库获取执行周期
                    String cron = getNewCron();
                    if (StrFuncs.isEmpty(cron)) {
                        return null;
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    /**
     * 子类重写该方法.
     */
    public void execute() {

    }

    /**
     * 动态获取调度周期.
     *
     * @return
     */
    public String getNewCron() {
        String newCron = generalDao.queryValue(String.class,
                "select cron from sys_schedule_config where task_class = ? and is_enable = 1", this.getClass().getSimpleName());
        return newCron;
    }

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
}
