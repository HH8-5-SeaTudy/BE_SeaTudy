package com.finalproject.seatudy.security;

import com.finalproject.seatudy.domain.entity.Member;
import com.finalproject.seatudy.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public UserDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + email));

        return new UserDetailsImpl(user);
    }
}