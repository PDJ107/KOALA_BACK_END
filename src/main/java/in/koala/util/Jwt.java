package in.koala.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Jwt {

    @Value("${spring.jwt.secret}")
    private String key;

    public String generateToken(Long id, String sub){

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg","HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("id", id );
        payloads.put("sub", sub);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if( sub.equals("access_token")) {
            calendar.add(Calendar.HOUR_OF_DAY, 24);
        }
        else{
            calendar.add(Calendar.DAY_OF_YEAR, 14);
        }
        Date exp = calendar.getTime();

        return Jwts.builder().setHeader(headers).setClaims(payloads).setExpiration(exp).signWith(SignatureAlgorithm.HS256, key.getBytes()).compact();
    }
}
