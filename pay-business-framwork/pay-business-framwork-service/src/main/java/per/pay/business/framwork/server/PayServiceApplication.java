package per.pay.business.framwork.server;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class);
    }

    @Bean
    public Gson buildGson(){
        Gson gson=new Gson();
        return gson;
    }

}
