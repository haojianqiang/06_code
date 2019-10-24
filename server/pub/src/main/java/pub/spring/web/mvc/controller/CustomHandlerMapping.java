package pub.spring.web.mvc.controller;

import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import pub.functions.StrFuncs;

import java.lang.reflect.Method;

/**
 * Modify by zzj on 2019/8/7.
 */
public class CustomHandlerMapping extends RequestMappingHandlerMapping {

    private String basePackageName;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if (info == null) {
            return info;
        }
        // 如果配置了映射路径则直接返回
        if (!info.getPatternsCondition().getPatterns().isEmpty()) {
            return info;
        }
        // 未配置则按规则自动生成
        // 获取控制类的路径
        String className = method.getDeclaringClass().getName();
        if (basePackageName == null) {
            int nPos = className.lastIndexOf(".web.");
            basePackageName = className.substring(0, nPos + 4);
        }
        assert className.startsWith(basePackageName);
        String uri = className.substring(basePackageName.length());

        // 判断控制类是否在action包下
        int lastDotPos = uri.lastIndexOf('.');
        int lastDotPos2 = uri.lastIndexOf('.', lastDotPos - 1);
        if (!"action".equals(uri.substring(lastDotPos2 + 1, lastDotPos))) {
            throw new RuntimeException("bad controller package");
        }

        // 判断控制类是否以Action结尾，并在路径中去掉
        String path = uri.substring(0, lastDotPos2);
        String actionName = uri.substring(lastDotPos + 1);
        if (!actionName.endsWith("Action")) {
            throw new RuntimeException("bad controller name");
        }
        actionName = actionName.substring(0, actionName.length() - 6);
        uri = path.replace('.', '/') + '/' + StrFuncs.unCamelize(actionName);

        // 如果起始目录是app则省略路径，保留
        if (uri.startsWith("/app/")) {
            uri = uri.substring(5);
        }

        // 设置返回的映射信息
        ParamsRequestCondition paramsRequestCondition = info.getParamsCondition();
        paramsRequestCondition = new ParamsRequestCondition("action=" + method.getName())
                .combine(paramsRequestCondition);
        String infoName = uri + '#' + method.getName();
//        System.out.println("================ infoName : " + infoName + " =====================");
        RequestMappingInfo info1 = new RequestMappingInfo(
                infoName,
                new PatternsRequestCondition(uri),
                info.getMethodsCondition(),
                paramsRequestCondition,
                info.getHeadersCondition(),
                info.getConsumesCondition(),
                info.getProducesCondition(),
                info.getCustomCondition()
        );

        return info1;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }
}
