import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 09:37
 */
public class Main {
    public static void main(String[] args) throws SchedulerException {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobDetail jobDetail = JobBuilder
                .newJob(LegendaryCarsBotJob.class)
                .storeDurably()
                .build();
        CronTrigger jobTrigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .forJob(jobDetail)
                .withSchedule(CronScheduleBuilder.cronSchedule(propertiesUtil.getProperty("cron-trigger")))
                .build();
        scheduler.addJob(jobDetail, true);
        scheduler.scheduleJob(jobTrigger);
        scheduler.start();
    }
}
