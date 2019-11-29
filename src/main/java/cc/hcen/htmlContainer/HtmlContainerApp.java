package cc.hcen.htmlContainer;

import com.alibaba.fastjson.JSON;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://hcen.cc:19000")
@RequestMapping("/html-container/api")
public class HtmlContainerApp implements HandlerInterceptor, WebMvcConfigurer {
    private static final int port = 19001;
    private static final String token = "123456";
    private String root = "/nginx/html-container";

    public static void main(String[] args) {
        SpringApplication.run(HtmlContainerApp.class);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(port);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).addPathPatterns("/**");
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        String token = request.getParameter("token");
        return token != null && token.equals(HtmlContainerApp.token);
    }

    @GetMapping("/create")
    public String create() {
        // 1. get dir name
        String dir = "null";
        // change to uuid
        dir = UUID.randomUUID().toString().replaceAll("-", "");
        if (isExists(dir)) {
            return "exist";
        } else {
            // create dir
            return createDir(dir) ? JSON.toJSONString(dir) : "create-fail";
        }
    }

    private boolean createDir(String dir) {
        return new File(getPath(dir)).mkdir();
    }

    private boolean isExists(String dir) {
        return new File(getPath(dir)).exists();
    }

    private String getPath(String dir) {
        return this.root + "/" + dir;
    }

    private String generateDirName(String dir) {
        String cmd = "head /dev/urandom | tr -dc A-Za-z0-9 | head -c 13 ; echo ''";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            proc.waitFor();
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            // Read the output from the command
            dir = stdInput.readLine();
            stdInput.close();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println("urandom error");
        }
        return dir;
    }

    @PostMapping("/update")
    public String update(@RequestParam String container,
                         @RequestParam String filename,
                         @RequestParam MultipartFile file) {
    // 要保证只能在容器里面上传
        if (!isInContainer(container)) {
            return "illegal";
        }
        //上传 或者覆盖一个文件 transferTo will delete the exist file first
        String dir = container + "/" + filename;
        // TODO 多级目录
        File local = new File(getPath(dir));
        // 新建
        try {
            file.transferTo(local);
            return dir;
        } catch (IOException e) {
            return "error in trans";
        }
    }

    private boolean isInContainer(String location) {
        String[] list = new File(this.root).list((dir, name) -> location.equals(name));
        return list!=null&& list.length==1;
    }

}
