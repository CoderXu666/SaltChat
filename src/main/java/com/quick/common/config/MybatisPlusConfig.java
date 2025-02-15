//package com.quick.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author 徐志斌
// * @Date: 2023/11/12 12:44
// * @Version 1.0
// * @Description: MyBatisPlus配置类
// */
//@Configuration
//public class MybatisPlusConfig {
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        interceptor.addInnerInterceptor(new (DbType.MYSQL));
//        return interceptor;
//    }
//}