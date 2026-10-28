package dental.flow.api.domain;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.dailymap.DailyMap;
import dental.flow.api.domain.inventory.Equipment;
import dental.flow.api.domain.inventory.InventoryProduct;
import dental.flow.api.domain.inventory.StockMovement;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryAndDailyMapRulesTest {
    @Test
    void shouldValidateEveryProductRule() {
        assertThrows(DomainException.class, () -> product(0L, "Luva", "UN", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, " ", "UN", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "x".repeat(101), "UN", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "Luva", " ", BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "Luva", "x".repeat(21), BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "Luva", "UN", new BigDecimal("-1"), BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "Luva", "UN", BigDecimal.ONE, new BigDecimal("-1"), BigDecimal.ZERO));
        assertThrows(DomainException.class, () -> product(1L, "Luva", "UN", BigDecimal.ONE, BigDecimal.ZERO, new BigDecimal("-1")));
        InventoryProduct p = product(1L, " Luva ", "un", null, null, null);
        assertEquals(BigDecimal.ZERO, p.getUnitCost());
        assertEquals("UN", p.getUnit());
        assertTrue(p.lowStock());
        assertTrue(p.getActive());
    }

    @Test
    void shouldValidateEveryEquipmentRule() {
        assertThrows(DomainException.class, () -> equipment(0L, "Raio", null, BigDecimal.ONE, 1, 0));
        assertThrows(DomainException.class, () -> equipment(1L, " ", null, BigDecimal.ONE, 1, 0));
        assertThrows(DomainException.class, () -> equipment(1L, "x".repeat(101), null, BigDecimal.ONE, 1, 0));
        assertThrows(DomainException.class, () -> equipment(1L, "Raio", "x".repeat(256), BigDecimal.ONE, 1, 0));
        assertThrows(DomainException.class, () -> equipment(1L, "Raio", null, new BigDecimal("-1"), 1, 0));
        assertThrows(DomainException.class, () -> equipment(1L, "Raio", null, BigDecimal.ONE, 0, 0));
        assertThrows(DomainException.class, () -> equipment(1L, "Raio", null, BigDecimal.ONE, 1, -1));
        Equipment e = equipment(1L, " Raio ", " ", new BigDecimal("10"), 4, null);
        assertEquals(new BigDecimal("2.50"), e.getCostPerUse());
        assertNull(e.getDescription());
        assertEquals(0, e.getCompletedUses());
        assertTrue(e.getActive());
    }

    @Test
    void shouldValidateMovementRules() {
        assertThrows(DomainException.class, () -> movement("x", BigDecimal.ONE, null));
        assertThrows(DomainException.class, () -> movement("ENTRADA", BigDecimal.ZERO, null));
        assertThrows(DomainException.class, () -> movement("SAIDA", new BigDecimal("-1"), null));
        assertThrows(DomainException.class, () -> movement("AJUSTE", BigDecimal.ZERO, "x".repeat(151)));
        StockMovement movement = movement(" entrada ", BigDecimal.ONE, " Compra ");
        assertEquals("ENTRADA", movement.type());
        assertEquals("Compra", movement.reason());
        assertNull(movement("AJUSTE", BigDecimal.ZERO, " ").reason());
    }

    @Test
    void shouldValidateDailyMapLinesAndFinancialInputs() {
        assertThrows(DomainException.class, () -> map(null, List.of(), List.of(), List.of(), BigDecimal.ZERO, 0, null));
        var valid = visit(1, "CLINICO", BigDecimal.TEN, true);
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(valid, valid), List.of(), List.of(), BigDecimal.ZERO, 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(visit(1, "OUTRO", BigDecimal.ONE, true)), List.of(), List.of(), BigDecimal.ZERO, 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(visit(1, "CLINICO", new BigDecimal("-1"), true)), List.of(), List.of(), BigDecimal.ZERO, 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(), List.of(new DailyMap.Voucher(null, null, null, new BigDecimal("-1"), null, true)), List.of(), BigDecimal.ZERO, 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(), List.of(), List.of(new DailyMap.Expense(null, " ", BigDecimal.ONE)), BigDecimal.ZERO, 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(), List.of(), List.of(), new BigDecimal("-1"), 0, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(), List.of(), List.of(), BigDecimal.ZERO, -1, null));
        assertThrows(DomainException.class, () -> map(LocalDate.now(), List.of(), List.of(), List.of(), BigDecimal.ZERO, 0, "x".repeat(4001)));
        DailyMap empty = map(LocalDate.now(), null, null, null, null, null, " ");
        assertEquals(BigDecimal.ZERO, empty.getGeneralGross());
        assertNull(empty.getNotes());
        empty.close(1L);
        assertThrows(DomainException.class, () -> empty.close(1L));
    }

    private InventoryProduct product(Long c, String n, String u, BigDecimal cost, BigDecimal stock, BigDecimal minimum) {
        return new InventoryProduct(c, n, u, cost, stock, minimum, null);
    }

    private Equipment equipment(Long c, String n, String d, BigDecimal value, Integer life, Integer uses) {
        return new Equipment(c, n, d, value, life, uses, null);
    }

    private StockMovement movement(String type, BigDecimal q, String reason) {
        return new StockMovement(null, 1L, 1L, type, q, BigDecimal.ONE, reason, null);
    }

    private DailyMap.Visit visit(int line, String type, BigDecimal value, boolean checked) {
        return new DailyMap.Visit(null, null, null, 2L, null, line, null, null, null, null, null, null, null, value, type, checked);
    }

    private DailyMap map(LocalDate date, List<DailyMap.Visit> visits, List<DailyMap.Voucher> vouchers, List<DailyMap.Expense> expenses, BigDecimal discount, Integer count, String notes) {
        return new DailyMap(1L, 2L, 1L, date, discount, null, null, null, count, null, null, null, null, notes, visits, vouchers, expenses, null);
    }
}
