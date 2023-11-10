package com.is1di.courses.service;

import com.is1di.courses.utils.MessageMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messages;

    public String getMessage(MessageMethod method, String... args){
        Locale locale = LocaleContextHolder.getLocale();
        return messages.getMessage(method.getVal(),args,locale);
    }
    public String getMessage(MessageMethod method){
        Locale locale = LocaleContextHolder.getLocale();
        return messages.getMessage(method.getVal(),null,locale);
    }
}
