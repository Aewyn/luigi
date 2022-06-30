package com.example.luigi.sessions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MandjeTest {
    private Mandje mandje;
    @BeforeEach void beforeEach(){
        mandje = new Mandje();
    }

    @Test
    @DisplayName("Een nieuw mandje is leeg")
    void eenNieuwMandjeIsLeeg(){
        assertThat(mandje.getIds()).isEmpty();
    }

    @Test
    @DisplayName("Nadat je een item toevoegd in het mandje bevat het mandje dit item")
    void nadatJeEenItemToevoegdInHetMandjeBevatHetMandjeDitItem(){
        mandje.voegToe(10L);
        assertThat(mandje.getIds()).containsOnly(10L);
    }

    @Test
    @DisplayName("Je kan een item maar een keer toevoegen aan het mandje")
    void jeKanEenItemMaarEenKeerToevoegenAanHetMandje(){
        mandje.voegToe(10L);
        mandje.voegToe(10L);
        assertThat(mandje.getIds()).containsOnly(10L);
    }

    @Test
    @DisplayName("Nadat je twee items in het mandje legt bevat het mandje enkel die items")
    void nadatJeTweeItemsInHetMandjeLegtBevatHetMandjeEnkelDieItems(){
        mandje.voegToe(10L);
        mandje.voegToe(20L);
        assertThat(mandje.getIds()).containsOnly(10L,20L);
    }
}