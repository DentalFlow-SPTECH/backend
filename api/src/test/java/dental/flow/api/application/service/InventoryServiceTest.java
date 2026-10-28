package dental.flow.api.application.service;

import dental.flow.api.adapters.rest.in.controller.dto.request.*;
import dental.flow.api.application.out.InventoryRepository;
import dental.flow.api.boot.exception.*;
import dental.flow.api.domain.inventory.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
 @Mock InventoryRepository repository; @InjectMocks InventoryService service;
 @Test void shouldCreateProductAndRegisterInitialStock(){var request=new SaveInventoryProductRequestDTO("Luva","caixa",new BigDecimal("25"),new BigDecimal("10"),new BigDecimal("2"),true);when(repository.saveProduct(any(),eq(7L),eq("CRIAR"))).thenAnswer(i->{InventoryProduct p=i.getArgument(0);p.setId(1L);return p;});when(repository.moveStock(eq(1L),eq(3L),eq(7L),eq("AJUSTE"),any(),any())).thenReturn(new StockMovement(1L,1L,7L,"AJUSTE",BigDecimal.TEN,new BigDecimal("25"),"Estoque inicial",LocalDateTime.now()));var response=service.createProduct(3L,7L,request);assertEquals("CAIXA",response.unit());assertFalse(response.lowStock());verify(repository).moveStock(1L,3L,7L,"AJUSTE",new BigDecimal("10"),"Estoque inicial");}
 @Test void shouldRejectDuplicatedProductAndInvalidValues(){when(repository.productNameExists(3L,"Luva",null)).thenReturn(true);assertThrows(BusinessException.class,()->service.createProduct(3L,7L,new SaveInventoryProductRequestDTO("Luva","UN",BigDecimal.ONE,BigDecimal.ZERO,BigDecimal.ZERO,true)));assertThrows(DomainException.class,()->service.createEquipment(3L,7L,new SaveEquipmentRequestDTO("Raio X",null,BigDecimal.TEN,0,0,true)));}
 @Test void shouldUpdateWithoutReplacingStockAndMoveActiveProduct(){InventoryProduct existing=product(1L,true);existing.setCurrentStock(new BigDecimal("8"));when(repository.findProduct(1L,3L)).thenReturn(Optional.of(existing));when(repository.saveProduct(any(),eq(7L),eq("ATUALIZAR"))).thenAnswer(i->i.getArgument(0));when(repository.moveStock(anyLong(),anyLong(),anyLong(),anyString(),any(),any())).thenReturn(new StockMovement(2L,1L,7L,"SAIDA",BigDecimal.ONE,new BigDecimal("5"),null,LocalDateTime.now()));var updated=service.updateProduct(1L,3L,7L,new SaveInventoryProductRequestDTO("Luva","UN",new BigDecimal("5"),new BigDecimal("999"),BigDecimal.ONE,true));assertEquals(new BigDecimal("8"),updated.currentStock());assertEquals("SAIDA",service.moveStock(1L,3L,7L,new StockMovementRequestDTO("saida",BigDecimal.ONE,null)).type());}
 @Test void shouldRejectMovementForInactiveProductAndHandleEquipment(){when(repository.findProduct(1L,3L)).thenReturn(Optional.of(product(1L,false)));assertThrows(BusinessException.class,()->service.moveStock(1L,3L,7L,new StockMovementRequestDTO("SAIDA",BigDecimal.ONE,null)));Equipment saved=new Equipment(3L,"Raio X",null,new BigDecimal("1000"),100,5,true);saved.setId(2L);when(repository.saveEquipment(any(),eq(7L),eq("CRIAR"))).thenReturn(saved);var response=service.createEquipment(3L,7L,new SaveEquipmentRequestDTO("Raio X",null,new BigDecimal("1000"),100,5,true));assertEquals(new BigDecimal("10.00"),response.costPerUse());}
 private InventoryProduct product(Long id,boolean active){InventoryProduct p=new InventoryProduct(3L,"Luva","UN",new BigDecimal("5"),BigDecimal.TEN,BigDecimal.ONE,active);p.setId(id);return p;}
}
