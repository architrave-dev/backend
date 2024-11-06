package com.architrave.portfolio.global.aop.ownerCheck;

import com.architrave.portfolio.domain.model.Member;
import org.springframework.stereotype.Component;

@Component
public class OwnerContextHolder {
    private static final ThreadLocal<Member> ownerTreadLocal = new ThreadLocal();

    public static void setOwner(Member owner){
        ownerTreadLocal.set(owner);
    };
    public static Member getOwner(){
        return ownerTreadLocal.get();
    };
    public static void clear(){
        ownerTreadLocal.remove();
    };
}
