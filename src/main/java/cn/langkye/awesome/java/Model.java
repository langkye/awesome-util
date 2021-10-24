package cn.langkye.awesome.java;

import cn.langkye.awesome.validate.HibernateValidateUtil;
//import jakarta.validation.constraints.NotBlank;

import javax.validation.constraints.NotBlank;

/**
 * @author langkye
 * @date 2021/10/20
 */
public class Model {
    public static void main(String[] args) {
        final Model model = new Model();
        HibernateValidateUtil.validateJavaBeanThrowsException(model);
    }

    @NotBlank(message = "name not blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
