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
    
    private ServiceService service;
    
    private ServiceRepository repository;
    
    @BeforeEach
    public void before() {
        this.repository = mock(ServiceRepository.class);
        this.service = new ServiceService(repository);
    }
    
    @Test
    protected void createServiceCaseNotExisting() {
        when(repository.read(any(Name.class))).thenReturn(Option.none());
        when(repository.create(any(Service.class))).thenReturn(Option.of(ServiceTest.service));
        
        var createService = new CreateService(null, NameTest.permittedValue);
        var response = service.create(createService);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> verify(repository).create(any(Service.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void deleteServiceByTitleCaseExisting() {
        when(repository.read(any(Name.class))).thenReturn(Option.of(ServiceTest.service));
        when(repository.delete(any(Service.class))).thenReturn(Option.of(ServiceTest.service));
        
        var response = service.delete(NameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> verify(repository).delete(any(Service.class)),
                  () -> assertFalse(response.hasError()));
    }
    
    @Test
    protected void returnErrorOnCreateCaseTitleExisting() {
        when(repository.read(any(Name.class))).thenReturn(Option.of(ServiceTest.service));
        when(repository.create(any(Service.class))).thenReturn(Option.of(ServiceTest.service));
        
        var createService = new CreateService(null, NameTest.permittedValue);
        var response = service.create(createService);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> verifyNoMoreInteractions(repository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.DUPLICATE, response.getError().getStatus()),
                  () -> assertEquals(Trace.TITLE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnDeleteByTitleCaseMissing() {
        when(repository.read(any(Name.class))).thenReturn(Option.none());
        when(repository.delete(any(Service.class))).thenReturn(Option.of(ServiceTest.service));
        
        var response = service.delete(NameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> verifyNoMoreInteractions(repository),
                  () -> assertTrue(response.hasError()),
                  () -> assertEquals(Status.MISSING, response.getError().getStatus()),
                  () -> assertEquals(Trace.DATABASE, response.getError().getTrace()));
    }
    
    @Test
    protected void returnErrorOnReadByTitleCaseMissing() {
        when(repository.read(any(Name.class))).thenReturn(Option.none());
        
        var readService = service.read(NameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> assertTrue(readService.hasError()),
                  () -> assertEquals(Status.MISSING, readService.getError().status),
                  () -> assertEquals(Trace.DATABASE, readService.getError().trace));
    }
    
    @Test
    protected void returnListOfReadServiceOnReadAll() {
        when(repository.readAll()).thenReturn(List.of(ServiceTest.service));
        when(repository.readAmount()).thenReturn(1L);
        
        var pagination = service.readAll();
        
        assertAll(() -> verify(repository).readAll(),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(1, pagination.getItems().size()),
                  () -> assertEquals(1, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()),
                  () -> assertEquals(ServiceTest.service.getIdentifier().getUuid(),
                                     pagination.getItems().get(0).getIdentifier()));
    }
    
    @Test
    protected void returnListOfReadServiceWithPaginationOnReadAll() {
        when(repository.readAll()).thenReturn(List.of(ServiceTest.service, ServiceTest.service));
        when(repository.readAmount()).thenReturn(1L);
        
        var pagination = service.readAll(1);
        
        assertAll(() -> verify(repository).readAll(any(Positive.class)),
                  () -> assertFalse(pagination.hasError()),
                  () -> assertEquals(0, pagination.getItems().size()),
                  () -> assertEquals(0, pagination.getNumberItems()),
                  () -> assertEquals(1, pagination.getNumberTotal()));
    }
    
    @Test
    protected void returnErrorOnReadAllWithZeroPagination() {
        var pagination = service.readAll(0);
        
        assertAll(() -> assertTrue(pagination.hasError()),
                  () -> assertEquals(Status.FORMATTING, pagination.getError().getStatus()),
                  () -> assertEquals(Trace.POSITIVE, pagination.getError().getTrace()));
    }
    
    @Test
    protected void returnReadServiceOnReadByTitleCaseExisting() {
        when(repository.read(any(Name.class))).thenReturn(Option.of(ServiceTest.service));
        
        var readService = service.read(NameTest.permittedValue);
        
        assertAll(() -> verify(repository).read(any(Name.class)),
                  () -> assertFalse(readService.hasError()),
                  () -> assertEquals(ServiceTest.service.getTitle().getValue(), readService.getItem().getTitle()),
                  () -> assertEquals(ServiceTest.service.getIdentifier().getUuid(),
                                     readService.getItem().getIdentifier()));
    }
    
    @Test
    protected void updateDescriptionCaseExistingAndDifferent() {
        when(repository.read(any(Name.class))).thenReturn(Option.of(ServiceTest.service));
        when(repository.update(any(Service.class))).thenReturn(Option.of(ServiceTest.service));
        
        var args = ArgumentCaptor.forClass(Service.class);
        var updateService = new UpdateService(NameTest.notStoredValue, null);
        var response = service.update(ServiceTest.service.getTitle().getValue(), updateService);
        
        assertAll(() -> assertFalse(response.hasError()),
                  () -> verify(repository).update(args.capture()),
                  () -> assertEquals(NameTest.notStoredValue, args.getValue().getDescription().getValue()),
                  () -> assertEquals(ServiceTest.service.getTitle(), args.getValue().getTitle()));
    }
    
}
