package dev.patika.library_management.business;


import dev.patika.library_management.core.mapper.PublisherMapper;
import dev.patika.library_management.dao.PublisherRepo;
import dev.patika.library_management.dto.response.PublisherResponse;
import dev.patika.library_management.entities.Publisher;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PublisherService {

    private final PublisherRepo publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepo publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    public List<PublisherResponse> findAll() {
        return publisherMapper.asOutput(publisherRepository.findAll());
    }

    public PublisherResponse getById(Long id) {
        Publisher p = publisherRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "id li Yayın Evi Bulunamadı !!!"));
        return publisherMapper.asOutput(p);
    }

    public Publisher create(Publisher request) {
        // Name , EstablishmentYear bizim için unique sayılıyor bu yüzden bu özelliklere sahip publisher var mı diye
        // DB den bakıyoruz yoksa create isteği atabiliriz.
        Optional<Publisher> isPublisherExist = publisherRepository.findByNameAndEstablishmentYear(request.getName(), request.getEstablishmentYear());

        if (isPublisherExist.isEmpty()) {
            return publisherRepository.save(request);
        }
        throw new RuntimeException("Bu yayın evi daha önce sisteme kayıt olmuştur !!!");
    }

    public Publisher update(Long id, Publisher request) {

        Optional<Publisher> publisherFromDb = publisherRepository.findById(id);

        Optional<Publisher> isPublisherExist = publisherRepository.findByNameAndEstablishmentYear(request.getName(), request.getEstablishmentYear());

        if (publisherFromDb.isEmpty()) {
            throw new RuntimeException(id + "Güncellemeye çalıştığınız yayın evi sistemde bulunamadı. !!!.");
        }

        if (isPublisherExist.isPresent()) {
            throw new RuntimeException("Bu yayın evi daha önce sisteme kayıt olmuştur !!!");
        }

        // id yi boş gönderdiğimizden dolayı yeni kayıt yaratmaması için böyle yaptık DTO kullansak böyle
        // problem yaşamazdık.
        request.setId(id);
        return publisherRepository.save(request);
    }

    public void deleteById(Long id) {
        Optional<Publisher> publisherFromDb = publisherRepository.findById(id);
        if (publisherFromDb.isPresent()) {
            publisherRepository.delete(publisherFromDb.get());
        } else {
            throw new RuntimeException(id + "id li Yayın Evi sistemde bulunamadı !!!");
        }
    }
}