package es.Util;

import es.entity.jpaEntity.UserEntity;
import es.repository.jpaRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TYF on 2018/5/31.
 */
public class IdentityUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookie = request.getCookies();
        for (int i = 0; i < cookie.length; i++) {
            Cookie cook = cookie[i];
            if (cook.getName().equalsIgnoreCase(cookieName)) { //获取键
                return cook.getValue().toString();    //获取值
            }
        }
        return null;
    }
}