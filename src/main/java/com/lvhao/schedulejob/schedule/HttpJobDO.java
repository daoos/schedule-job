package com.lvhao.schedulejob.schedule;

import com.lvhao.schedulejob.util.ElapsedTimeUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * Http任务封装
 *
 * @author: lvhao
 * @since: 2016-7-22 10:20
 */
@Component
public class HttpJobDO implements Job {
    private static final Logger log = LoggerFactory.getLogger(HttpJobDO.class);

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String POST_METHOD = "POST";
    public static final String GET_METHOD = "GET";

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * 构造 request
     * @param url
     * @param jsonParams
     * @return
     */
    public static Request buildRequest(String method,String url,String jsonParams){
        Request.Builder builder = new Request.Builder();
        Request request = null;
        if (Objects.equals(method.toUpperCase(),GET_METHOD)) {
            request = builder.url(url)
                    .get()
                    .build();
        }else{
            RequestBody body = RequestBody.create(JSON,Objects.nonNull(jsonParams) ? jsonParams : "");
            request = builder.url(url)
                    .post(body)
                    .build();
        }
        return request;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String url = String.valueOf(jobDataMap.get("url"));
        String method = String.valueOf(jobDataMap.get("method"));
        String jsonStr = String.valueOf(jobDataMap.get("jsonParams"));
        Request request = buildRequest(method,url,jsonStr);
        Response response = null;
        ElapsedTimeUtils.time(url);
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("http调用出错",e);
        } finally {
            log.info("method:{} | url:{} | params:{} | resp: {}",new Object[]{
                method,
                url,
                jsonStr,
                String.valueOf(response.body())
            });
            ElapsedTimeUtils.timeEnd(url);
        }
    }
}
