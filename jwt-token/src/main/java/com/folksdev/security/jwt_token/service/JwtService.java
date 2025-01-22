package com.folksdev.security.jwt_token.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.key}")
	private String SECRET;
	
	//TOKEN GENERATE ETME İŞLEMİ
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		//claims.put("can", "wia");
		
		return createToken(claims, userName);
	}
	
	
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims)      //claims ekle 
				.setSubject(userName)   //username ekle
				.setIssuedAt(new Date(System.currentTimeMillis()))  //Token oluşturulma zamanı
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))   //Token geçerlilik süresi(5dk)
				.signWith(getSignKey(), SignatureAlgorithm.HS256)  //signature key
				.compact(); //Token'ı oluştur ve geri dön
	}
	
	
	//Byte ile 'Key' üretimi(HMAC key oluştururuz.)
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	
	
	//Gelen Token'ı validate etme işlemi -> Gelen Token'ı validate etmeden önce extract etmemiz gerekir. Yani 'payload'da neler var çözmemiz gerekir.(Payload'da bizim için önemli olan 'username' ve 'expireDate'dir. 'expireDate' hala geçerliyse validateToken'ı "true" döneceğiz değilse "false")
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		String username = extractUser(token);
		Date expirationDate = extractExpiration(token);
		
		return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());   //1.koşul; Sistemde kayıtlı kullanıcının username'i,[userDetails.getUsername()] ile token'dan çıkarılan kullanıcı adı (username) karşılaştırılır. Eğer eşleşmiyorsa, token geçersizdir. 
																								   //2.koşul; Token'in son kullanım tarihi, şu anki tarihten önce mi kontrol edilir.  Eğer token'in geçerlilik süresi dolmuşsa (expirationDate.before(new Date()) == true), token geçersizdir.
	}
	
	//Gelen Token'ın ne zaman expire olduğunu dönen bir method yazarız.(Yani gelen token içerisindeki expireDate'i alacağız.)
	private Date extractExpiration(String token) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())   //"signature key"i ekle
				.build()                       //Parser'ı oluştur
				.parseClaimsJws(token)         //Token'i çöz
				.getBody();                    //Claims'leri al
				
		return claims.getExpiration();         //Bu kodla; gelen token içerisindeki expireDate'i alacağız. Onu da yukarıda createToken'da ayarladığımız "setExpiration()"dan[token geçerlilik süresini(5dk)]' alırız.
    }
	
	//Yukarıda Date'i(expireDate) extract ettik. Şimdi aynı şekilde payload'dan "username"i extract edeceğiz.
	public String extractUser(String token) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())   //"signature key"i ekle
				.build()                       //Parser'ı oluştur
				.parseClaimsJws(token)         //Token'i çöz
				.getBody();                    //Claims'leri al
				
		return claims.getSubject();         //Bu kodla; gelen token içerisindeki username'i alacağız. Onu da yukarıda createToken'da ayarladığımız "setSubject()"den alırız.
    }
	
	

}
