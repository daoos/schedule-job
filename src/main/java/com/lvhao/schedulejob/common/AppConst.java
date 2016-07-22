package com.lvhao.schedulejob.common;

/**
 * 系统常量类
 *
 * @author: lvhao
 * @since: 2016-4-15 16:43
 */
public class AppConst {
    public static final class DBType {
        public static final String DEFAULT = "default";
        public static final String WRITE = "write";
        public static final String READ = "read";
    }

    public static final class JobType {
        public static final String HTTP_JOB = "http_job";
        public static final String THRIFT_JOB = "thrift_job";
    }
}
