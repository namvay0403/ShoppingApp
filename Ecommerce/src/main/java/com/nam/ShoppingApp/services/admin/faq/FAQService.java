package com.nam.ShoppingApp.services.admin.faq;

import com.nam.ShoppingApp.dto.FAQDto;

public interface FAQService {
    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
