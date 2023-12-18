package de.ottorohenkohl.domain.service;

import de.ottorohenkohl.domain.model.entity.Service;
import de.ottorohenkohl.domain.model.entity.ServiceTest;
import de.ottorohenkohl.domain.model.enumeration.Status;
import de.ottorohenkohl.domain.model.enumeration.Trace;
import de.ottorohenkohl.domain.model.value.primitive.Name;
import de.ottorohenkohl.domain.model.value.primitive.NameTest;
import de.ottorohenkohl.domain.model.value.primitive.Positive;
import de.ottorohenkohl.domain.repository.ServiceRepository;
import de.ottorohenkohl.domain.transfer.object.service.CreateService;
import de.ottorohenkohl.domain.transfer.object.service.UpdateService;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ServiceServiceTest {
    
    private static final Name description = new NameTest().getAbsentInstance();
    
    private static final Name title = new NameTest().getAbsentInstance();
    
    private static final Service service = new ServiceTest().getAbsentInstance();
    
    private ServiceService serviceService;
    
    private ServiceRepository serviceRepository;
    
    @BeforeEach
    public void before() {
        this.serviceRepository = mock(ServiceRepository.class);
        this.serviceService = new ServiceService(serviceRepository);
    }
    
    @Test
    protected void createServiceCaseNotExisting() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.none());
        when(serviceRepository.create(any(Service.class))).thenReturn(Option.of(service));
        
        var createService = new CreateService(null, title.getValue());
        var response = serviceService.create(createService);
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> verify(serviceRepository).create(any(Service.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void deleteServiceByTitleCaseExisting() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.of(service));
        when(serviceRepository.delete(any(Service.class))).thenReturn(Option.of(service));
        
        var response = serviceService.delete(title.getValue());
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> verify(serviceRepository).delete(any(Service.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void returnErrorOnCreateCaseTitleExisting() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.of(service));
        when(serviceRepository.create(any(Service.class))).thenReturn(Option.of(service));
        
        var createService = new CreateService(null, title.getValue());
        var response = serviceService.create(createService);
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> verifyNoMoreInteractions(serviceRepository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.DUPLICATE, response.getError().getStatus()),
                  () -> assertEquals(Trace.TITLE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnDeleteByTitleCaseMissing() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.none());
        when(serviceRepository.delete(any(Service.class))).thenReturn(Option.of(service));
        
        var response = serviceService.delete(title.getValue());
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> verifyNoMoreInteractions(serviceRepository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.MISSING, response.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnReadByTitleCaseMissing() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.none());
        
        var readService = serviceService.read(title.getValue());
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> assertTrue(readService.hasError()),
                  () -> assertEquals(Status.MISSING, readService.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, readService.getError().getTrace()));
    }
    
    @Test
    protected void returnListOfReadServiceOnReadAll() {
        when(serviceRepository.readAll()).thenReturn(List.of(service, service));
        when(serviceRepository.readAmount()).thenReturn(2L);
        
        var pagination = serviceService.readAll();
        
        assertAll(() -> verify(serviceRepository).readAll(),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(2, pagination.getItems().size()),
                  () -> assertEquals(2, pagination.getNumberItems()),
                  () -> assertEquals(2, pagination.getNumberTotal()),
                  () -> assertEquals(service.getIdentifier().getUuid(),
                                     pagination.getItems().get(0).getIdentifier()));
    }
    
    @Test
    protected void returnListOfReadServiceWithPaginationOnReadAll() {
        when(serviceRepository.readAmount()).thenReturn(2L);
        when(serviceRepository.readAll(eq(Positive.build(1).get()))).thenReturn(List.of(service));
        
        var pagination = serviceService.readAll(1);
        
        assertAll(() -> verify(serviceRepository).readAll(any(Positive.class)),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(1, pagination.getItems().size()),
                  () -> assertEquals(1, pagination.getNumberItems()),
                  () -> assertEquals(2, pagination.getNumberTotal()));
    }
    
    @Test
    protected void returnReadServiceOnReadByTitleCaseExisting() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.of(service));
        
        var readService = serviceService.read(title.getValue());
        
        assertAll(() -> verify(serviceRepository).read(any(Name.class)),
                  () -> assertFalse(readService.hasError()),
                  () -> assertEquals(title.getValue(), readService.getItem().getTitle()),
                  () -> assertEquals(service.getIdentifier().getUuid(),
                                     readService.getItem().getIdentifier()));
    }
    
    @Test
    protected void updateDescriptionCaseExistingAndDifferent() {
        when(serviceRepository.read(any(Name.class))).thenReturn(Option.of(service));
        when(serviceRepository.update(any(Service.class))).thenReturn(Option.of(service));
        
        var args = ArgumentCaptor.forClass(Service.class);
        var updateService = new UpdateService(description.getValue());
        var response = serviceService.update(title.getValue(), updateService);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(serviceRepository).update(args.capture()),
                  () -> assertEquals(description, args.getValue().getDescription()),
                  () -> assertEquals(title, args.getValue().getTitle()));
    }
    
}
