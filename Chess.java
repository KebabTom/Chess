
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import View.*;
import Model.*;
import Controller.*;

class Chess {



    public static void main(String[] args) {
        ChessController program = new ChessController();
        SwingUtilities.invokeLater(program);
    }
}