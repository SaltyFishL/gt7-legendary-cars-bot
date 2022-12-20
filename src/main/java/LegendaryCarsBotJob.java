import jakarta.mail.MessagingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import pojo.Car;
import pojo.LegendDealerShip;

import java.io.IOException;
import java.util.*;

/**
 * @author lujunqi
 * @version 1.0
 * @date 2022/12/20 16:42
 */
public class LegendaryCarsBotJob implements Job {

    Gt7InfoCollector gt7InfoCollector = new Gt7InfoCollector();
    MailSender mailSender = new MailSender();
    Set<String> targetCarIds = new HashSet<>();

    {
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        String[] ids = propertiesUtil.getProperty("target-car-ids").split(",");
        targetCarIds.addAll(Arrays.asList(ids));
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LegendDealerShip todayLegendDealerShip;
        try {
            todayLegendDealerShip = gt7InfoCollector.getTodayLegendDealerShip();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<Car> carList = todayLegendDealerShip.getCars();
        List<Car> targetCars = new ArrayList<>();
        for (Car car : carList) {
            if (targetCarIds.contains(car.getCarId())
                    && "normal".equals(car.getState())
                    && car.getEstimateDays() >= 0) {
                targetCars.add(car);
            }
        }
        if (targetCars.isEmpty()) {
            return;
        }
        StringBuilder mailContentBuilder = new StringBuilder();
        for (Car targetCar : targetCars) {
            mailContentBuilder.append(targetCar.getManufacturer())
                    .append(" ")
                    .append(targetCar.getName())
                    .append("  售价: Cr.")
                    .append(targetCar.getCredits())
                    .append(". 本次发售还剩: ")
                    .append(targetCar.getEstimateDays())
                    .append("天结束\n");
        }
        String subject = "愿望单中有" + targetCars.size() + "辆车正在出售";
        try {
            mailSender.sendEmail(subject, mailContentBuilder.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
