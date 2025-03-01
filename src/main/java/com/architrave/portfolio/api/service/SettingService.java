package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.auth.response.MemberSearchDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MenuVisible;
import com.architrave.portfolio.domain.model.Setting;
import com.architrave.portfolio.domain.model.builder.SettingBuilder;
import com.architrave.portfolio.domain.repository.SettingRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Trace
@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    /**
     * 내부용
     * @param  settingId
     * @return Setting
     */
    @Transactional(readOnly = true)
    public Setting findSettingById(Long settingId) {
        return settingRepository.findById(settingId)
                .orElseThrow(() -> new NoSuchElementException("there is no setting that id: " + settingId));
    }

    /**
     * @param  member
     * @return Setting
     */
    @Transactional
    public Setting findSettingByMember(Member member) {
        Setting setting = settingRepository.findByMember(member)
                .orElse(null);
        if(setting != null){
            return setting;
        }
        Setting defaultSetting = new SettingBuilder()
                .member(member)
                .pageName(member.getUsername())
                .pageVisible(true)
                .menuVisible(new MenuVisible(true, true, true, true))
                .build();

        return settingRepository.save(defaultSetting);
    }

    @Transactional
    public Setting updateSetting(
            Long id,
            String pageName,
            Boolean pageVisible,
            Boolean projects,
            Boolean works,
            Boolean about,
            Boolean contact
    ){
        Setting setting = findSettingById(id);
        if(!setting.getPageName().equals(pageName)) setting.setPageName(pageName);
        if(!setting.getPageVisible().equals(pageVisible)) setting.setPageVisible(pageVisible);

        MenuVisible menuVisible = setting.getMenuVisible();
        if (!Objects.equals(menuVisible.getProjects(), projects)) {
            menuVisible.setProjects(projects);
        }
        if (!Objects.equals(menuVisible.getWorks(), works)) {
            menuVisible.setWorks(works);
        }
        if (!Objects.equals(menuVisible.getAbout(), about)) {
            menuVisible.setAbout(about);
        }
        if (!Objects.equals(menuVisible.getContact(), contact)) {
            menuVisible.setContact(contact);
        }
        return setting;
    }

    @Transactional(readOnly = true)
    public List<MemberSearchDto> searchMembersByUsernamePrefix(String query) {
        List<Setting> settings = settingRepository.findByUsernamePrefixAndPageVisibleTrue(query);

        return settings.stream()
                .map(s -> new MemberSearchDto(s.getMember()))
                .collect(Collectors.toList());
    }
}
