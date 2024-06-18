package com.nam.ShoppingApp.services.admin.faq;

import com.nam.ShoppingApp.dto.FAQDto;
import com.nam.ShoppingApp.entity.FAQ;
import com.nam.ShoppingApp.entity.Product;
import com.nam.ShoppingApp.repository.FAQRepository;
import com.nam.ShoppingApp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FAQServiceImpl implements FAQService{

    @Autowired private FAQRepository faqRepository;

    @Autowired private ProductRepository productRepository;

    public FAQDto postFAQ(Long productId, FAQDto faqDto){
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isPresent()){
            FAQ faq = new FAQ();
            faq.setProduct(optionalProduct.get());
            faq.setQuestion(faqDto.getQuestion());
            faq.setAnswer(faqDto.getAnswer());

            return faqRepository.save(faq).getFAQDto();
        }
        return null;
    }

}
