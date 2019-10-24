package pub.spring.web.mvc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import pub.spring.web.mvc.model.ModelResult;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST 结果封装类
 */
public class ActionResult implements Serializable,ModelResult{
    private Boolean ok = true;
    private String msg;
    private Object data;
    private Map<String,String> errors;

    public ActionResult(){
        errors = new HashMap<String, String>();
    }

    public ActionResult(Object data){
        this();
        this.data = data;
    }

    public static ActionResult error(Errors errors){
        ActionResult result = new ActionResult();
        result.setOk(false);
        HashMap<String, String> errorMap = new HashMap<String, String>();
        List<ObjectError> objErrors = errors.getAllErrors();
        for (ObjectError objError : objErrors) {
            if (objError instanceof FieldError) {
                FieldError fieldError = (FieldError) objError;
                errorMap.put(fieldError.getField(),
                        fieldError.getDefaultMessage());
            }
        }
        result.setErrors(errorMap);
        return result;
    }

    public static ActionResult error(Exception e) {
        ActionResult result = new ActionResult();
        result.setOk(false);
        result.setMsg(e.getClass().getName() + ":\n" + e.getMessage());
        return result;
    }

    public static ActionResult error(String msg) {
        ActionResult result = new ActionResult();
        result.setOk(false);
        result.setMsg(msg);
        return result;
    }

    public static ActionResult ok(String msg, Object data) {
        ActionResult result = new ActionResult();
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static ActionResult ok(String msg) {
        return ok(msg, null);
    }

    public static ActionResult ok() {
        return ok(null);
    }

    public static ActionResult okWithData(Object data) {
        return ok(null, data);//msg提示信息
    }

    public static ActionResult fail(Exception e) {
        return fail(e.getMessage());
    }

    public static ActionResult fail(String message) {
        ActionResult result = new ActionResult();
        result.setOk(false);
        result.setMsg(message);
        return result;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    @JsonIgnore
    public boolean isDirectResult() {
        return false;
    }
}
