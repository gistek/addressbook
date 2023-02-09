package org.example.addressbook.controller;

import org.example.addressbook.model.AddressBook;
import org.example.addressbook.repository.AddressBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AddressBookController {
    private static final Logger logger = LoggerFactory.getLogger(AddressBookController.class);
    final AddressBookRepository addressBookRepository;

    public AddressBookController(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/test")
    public String getTest(){
        return "Hello World";
    }

    @GetMapping("/reactive-test")
    public Mono<String> getReactiveTest(){
        return Mono.just("Hello reactive World!");
    }

    @GetMapping("/addressbooks")
    public Flux<AddressBook> getAddressBook(@RequestParam("page") Optional<Integer> pageOpt,
                                            @RequestParam("size") Optional<Integer> sizeOpt){
        final PageRequest pageRequest = PageRequest.of(
                pageOpt.orElse(0),
                sizeOpt.orElse(100));
        logger.info("Show addressbooks {}", pageRequest);
        return addressBookRepository.findAllBy(pageRequest);
    }

    @PostMapping("/addressbook")
    public Mono<AddressBook> save(@RequestBody AddressBook addressBook){
        logger.info("Save addressbook, {}", addressBook);
        return addressBookRepository.save(addressBook);
    }


}
