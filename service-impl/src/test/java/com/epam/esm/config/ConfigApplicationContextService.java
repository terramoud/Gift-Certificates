package com.epam.esm.config;


import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.repository.api.CertificateRepository;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.api.CertificateService;
import com.epam.esm.service.api.TagService;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigApplicationContextService {
    @Bean(name = "mockCertificateRepository")
    public CertificateRepository mockCertificateRepository() {
        return Mockito.mock(CertificateRepository.class);
    }

    @Bean(name = "mockTagRepository")
    public TagRepository mockTagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean(name = "mockParametersMapper")
    public RequestParametersMapper parametersMapper() {
        return Mockito.mock(RequestParametersMapper.class);
    }

    @Bean(name = "mockCertificateMapper")
    public CertificateMapper certificateMapper() {
        return Mockito.mock(CertificateMapper.class);
    }

    @Bean
    public TagService tagService() {
        return new TagServiceImpl(parametersMapper(), mockTagRepository());
    }

    @Bean
    public CertificateService certificateService() {
        return new CertificateServiceImpl(
                mockCertificateRepository(),
                mockTagRepository(),
                parametersMapper(),
                certificateMapper());
    }
}
