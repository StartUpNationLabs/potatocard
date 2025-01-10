package com.polytech.jc;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.io.IOException;
import java.util.List;

public class Screen {
    public static void main(String[] args) {
        try {
            // Initialisation du terminal et de l'écran
            com.googlecode.lanterna.screen.Screen screen = new DefaultTerminalFactory().createScreen();
            screen.startScreen();
            screen.clear();

            screen.newTextGraphics()
                    .setBackgroundColor(TextColor.ANSI.DEFAULT)
                    .setForegroundColor(TextColor.ANSI.DEFAULT)
                    .putString(10, 5, "En attente d'une carte...", SGR.BOLD);

            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            System.out.println("Terminals : " + terminals);

            screen.refresh();

            CardTerminal terminal = terminals.get(0);
            terminal.waitForCardPresent(0);

            screen.clear();

            // Les éléments du menu
            String[] menuItems = {"Acheter des produits", "Consulter le solde", "Quitter"};
            int selectedItem = 0;  // L'élément actuellement sélectionné

            boolean running = true;

            while (running) {
                System.out.println(terminal.isCardPresent());
                // Afficher le menu avec l'élément sélectionné en surbrillance
                for (int i = 0; i < menuItems.length; i++) {
                    if (i == selectedItem) {
                        screen.newTextGraphics()
                                .setBackgroundColor(TextColor.ANSI.BLUE)
                                .setForegroundColor(TextColor.ANSI.WHITE)
                                .putString(10, 5 + i, menuItems[i], SGR.BOLD);
                    } else {
                        screen.newTextGraphics()
                                .setBackgroundColor(TextColor.ANSI.DEFAULT)
                                .setForegroundColor(TextColor.ANSI.DEFAULT)
                                .putString(10, 5 + i, menuItems[i]);
                    }
                }

                screen.refresh();

                // Lire l'entrée de l'utilisateur
                switch (screen.readInput().getKeyType()) {
                    case ArrowDown:
                        selectedItem = (selectedItem + 1) % menuItems.length;  // Descendre dans la liste
                        break;
                    case ArrowUp:
                        selectedItem = (selectedItem - 1 + menuItems.length) % menuItems.length;  // Monter dans la liste
                        break;
                    case Enter:
                        if (selectedItem == 0) {
                            screen.clear();
                            screen.newTextGraphics().putString(10, 5, "Produit acheté!", SGR.BOLD);
                            screen.refresh();
                        } else if (selectedItem == 1) {
                            screen.clear();
                            screen.newTextGraphics().putString(10, 5, "Votre solde: 10€", SGR.BOLD);
                            screen.refresh();
                        } else if (selectedItem == 2) {
                            running = false;  // Quitter
                        }
                        screen.readInput();  // Attendre une entrée avant de revenir au menu
                        break;
                    case Escape:
                        running = false;  // Quitter
                        break;
                }

                screen.clear();  // Effacer l'écran avant de redessiner le menu
            }

            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CardException e) {
            throw new RuntimeException(e);
        }
    }
}
