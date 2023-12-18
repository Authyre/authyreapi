package de.ottorohenkohl.domain.service;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.embedded.Error;
import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.model.value.primitive.Primitive;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import de.ottorohenkohl.domain.transfer.object.service.CreateService;
import de.ottorohenkohl.domain.transfer.object.service.ReadService;
import de.ottorohenkohl.domain.transfer.object.service.UpdateService;
import de.ottorohenkohl.domain.transfer.response.Empty;
import de.ottorohenkohl.domain.transfer.response.Fetched;
import de.ottorohenkohl.domain.transfer.response.Pagination;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Dependent
public class ServiceService {
    
    private final ServiceRepository serviceRepository;
    
    @Inject
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    
    private static Validation<Error, Service> insertDescriptionOrLeaveNull(Service service, String description) {
        return Option.of(description)
                     .toEither(Validation.<Error, Service> valid(service))
                     .map(Name::build)
                     .map(u -> u.peek(service::setDescription).map(v -> service))
                     .fold(u -> u, u -> u);
    }
    
    public Empty create(CreateService createService) {
        return Name.build(createService.getTitle())
                   .map(Service::new)
                   .filter(t -> serviceRepository.read(t.getTitle()).isEmpty())
                   .toValidation(new Error(Status.DUPLICATE, Trace.TITLE))
                   .flatMap(t -> t)
                   .map(t -> insertDescriptionOrLeaveNull(t, createService.getDescription()))
                   .flatMap(t -> t)
                   .peek(serviceRepository::create)
                   .fold(Empty::new, t -> new Empty());
    }
    
    public Empty delete(String title) {
        return Name.build(title)
                   .map(serviceRepository::read)
                   .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                   .map(serviceRepository::delete)
                   .flatMap(t -> t.toValidation(new Error(Status.INTERNAL, Trace.DATABASE)))
                   .fold(Empty::new, t -> new Empty());
    }
    
    @Transactional
    public Empty update(String title, UpdateService updateService) {
        return Name.build(title)
                   .map(serviceRepository::read)
                   .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                   .map(t -> update(t, updateService))
                   .fold(Empty::new, u -> u);
        
    }
    
    public Fetched<ReadService> read(String title) {
        return Name.build(title)
                   .map(serviceRepository::read)
                   .flatMap(t -> t.toValidation(new Error(Status.MISSING, Trace.DATABASE)))
                   .map(ReadService::new)
                   .fold(Fetched::new, Fetched::new);
    }
    
    public Pagination<ReadService> readAll() {
        return new Pagination<>(serviceRepository.readAmount(),
                                serviceRepository.readAll().stream().map(ReadService::new).toList());
    }
    
    public Pagination<ReadService> readAll(Integer pages) {
        return Positive.build(pages)
                       .fold(Pagination::new,
                             t -> new Pagination<>(serviceRepository.readAmount(),
                                                   serviceRepository.readAll(t)
                                                                    .stream()
                                                                    .map(ReadService::new)
                                                                    .toList()));
    }
    
    private Empty update(Service service, UpdateService updateService) {
        return Primitive.update(Name::build, service::getDescription, updateService.getDescription())
                        .map((description) -> {
                            service.setDescription(description);
                            
                            serviceRepository.update(service);
                            
                            return new Empty();
                        })
                        .fold(Empty::new, u -> u);
    }
    
}
