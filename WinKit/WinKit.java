import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;
import javax.imageio.ImageIO;

public class WinKit {

    // --- ВАША ССЫЛКА НА DONATIONALERTS ---
    // ЗАМЕНИТЕ ЭТУ ССЫЛКУ НА СВОЮ!
    private static final String DONATE_URL = "https://www.donationalerts.com/r/wixight"; 

    // --- НАСТРОЙКИ СОСТОЯНИЯ ---
    private static boolean isDarkTheme = true;
    private static boolean isRussian = true;

    // --- ЦВЕТОВАЯ ПАЛИТРА ---
    static Color CLR_BG_MAIN;
    static Color CLR_BG_SIDEBAR;
    static Color CLR_ACCENT;
    static Color CLR_TEXT;
    static Color CLR_TILE;
    static Color CLR_TILE_TEXT;
    static Color CLR_PANEL_BG;
    
    static final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private static JPanel mainContent;
    private static CardLayout cardLayout;
    private static JFrame frame;

    private static final Map<String, String[]> LANG_MAP = new HashMap<>();

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        initLang();
        applyThemeColors();

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("WinKit");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 750);
            frame.setLayout(new BorderLayout());

            reloadUI("HOME");

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void reloadUI(String targetPage) {
        frame.getContentPane().removeAll();
        frame.getContentPane().setBackground(CLR_BG_MAIN);

        JPanel sidebar = createSidebar();
        frame.add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(CLR_BG_MAIN);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainContent.add(createHomeTiles(), "HOME");
        mainContent.add(createPipettePanel(), "PIPETTE");
        mainContent.add(createArchiverPanel(), "ARCHIVER");
        mainContent.add(createConverterPanel(), "CONVERTER");
        mainContent.add(createNotesPanel(), "NOTES");
        mainContent.add(createSettingsPanel(), "SETTINGS");
        mainContent.add(createAboutPanel(), "ABOUT");
        mainContent.add(createDonatePanel(), "DONATE"); // НОВЫЙ РАЗДЕЛ

        frame.add(mainContent, BorderLayout.CENTER);
        
        cardLayout.show(mainContent, targetPage);
        frame.revalidate();
        frame.repaint();
    }

    private static void applyThemeColors() {
        if (isDarkTheme) {
            CLR_BG_MAIN = new Color(43, 43, 43);
            CLR_BG_SIDEBAR = new Color(55, 58, 60);
            CLR_ACCENT = new Color(75, 110, 175);
            CLR_TEXT = new Color(230, 230, 230);
            CLR_TILE = new Color(210, 210, 210);
            CLR_TILE_TEXT = new Color(30, 30, 30);
            CLR_PANEL_BG = new Color(60, 60, 60);
        } else {
            CLR_BG_MAIN = new Color(240, 240, 240);
            CLR_BG_SIDEBAR = new Color(220, 220, 220);
            CLR_ACCENT = new Color(0, 120, 215);
            CLR_TEXT = new Color(30, 30, 30);
            CLR_TILE = new Color(255, 255, 255);
            CLR_TILE_TEXT = new Color(0, 0, 0);
            CLR_PANEL_BG = new Color(255, 255, 255);
        }
    }

    private static void initLang() {
        LANG_MAP.put("sidebar.menu", new String[]{"Menu", "Меню"});
        LANG_MAP.put("sidebar.home", new String[]{"Home", "Главная"});
        LANG_MAP.put("sidebar.settings", new String[]{"Settings", "Настройки"});
        LANG_MAP.put("sidebar.theme", new String[]{"Theme", "Тема"});
        LANG_MAP.put("sidebar.lang", new String[]{"Language", "Язык"});
        LANG_MAP.put("sidebar.info", new String[]{"Info", "Инфо"});
        LANG_MAP.put("sidebar.about", new String[]{"About Us", "О нас"});
        LANG_MAP.put("sidebar.donate", new String[]{"Support Us", "Поддержать нас"}); // НОВОЕ

        LANG_MAP.put("tile.pipette", new String[]{"Pipette", "Пипетка"});
        LANG_MAP.put("tile.archiver", new String[]{"Archiver", "Архиватор"});
        LANG_MAP.put("tile.converter", new String[]{"Converter", "Конвертер"});
        LANG_MAP.put("tile.notes", new String[]{"Notes", "Заметки"});

        LANG_MAP.put("pipette.btn", new String[]{"Pick Color", "Захватить цвет"});
        LANG_MAP.put("pipette.copyHex", new String[]{"Copy HEX", "Копир. HEX"});
        LANG_MAP.put("pipette.copyRgb", new String[]{"Copy RGB", "Копир. RGB"});
        
        LANG_MAP.put("arch.zip", new String[]{"Pack to ZIP", "Собрать в ZIP"});
        LANG_MAP.put("arch.unzip", new String[]{"Unpack ZIP", "Распаковать ZIP"});
        LANG_MAP.put("arch.success", new String[]{"Archive created!", "Архив создан!"});
        LANG_MAP.put("arch.done", new String[]{"Unpacking done!", "Распаковка завершена!"});

        LANG_MAP.put("conv.title", new String[]{"Image Converter", "Конвертер изображений"});
        LANG_MAP.put("conv.load", new String[]{"1. Load Image", "1. Загрузить фото"});
        LANG_MAP.put("conv.format", new String[]{"Output Format:", "Формат выхода:"});
        LANG_MAP.put("conv.save", new String[]{"2. Save As...", "2. Сохранить как..."});
        LANG_MAP.put("conv.nofile", new String[]{"No file selected", "Файл не выбран"});
        LANG_MAP.put("conv.selected", new String[]{"Selected: ", "Выбран: "});

        LANG_MAP.put("notes.title", new String[]{"Quick Notes", "Заметки"});
        LANG_MAP.put("notes.new", new String[]{"New Note", "Новая заметка"});
        LANG_MAP.put("notes.close", new String[]{"Close Tab", "Закрыть вкладку"});

        LANG_MAP.put("set.theme", new String[]{"Interface Theme", "Тема интерфейса"});
        LANG_MAP.put("set.lang", new String[]{"Interface Language", "Язык интерфейса"});
        LANG_MAP.put("set.dark", new String[]{"Dark", "Темная"});
        LANG_MAP.put("set.light", new String[]{"Light", "Светлая"});

        LANG_MAP.put("donate.title", new String[]{"Support the Developers", "Поддержать разработчиков"});
        LANG_MAP.put("donate.desc", new String[]{
            "<html><center>If you like WinKit, you can support us via DonationAlerts.<br>Every donation motivates us to make updates!</center></html>", 
            "<html><center>Если вам нравится WinKit, вы можете поддержать нас через DonationAlerts.<br>Любой донат мотивирует нас пилить обновы!</center></html>"
        });
        LANG_MAP.put("donate.btn", new String[]{"Donate via DonationAlerts", "Отправить донат"});

        String aboutRu = "<html><center>Мы друзья студенты которые сделали для вас<br>такой шедевральный проект.<br><br><b>Наши telegram для связи:</b><br>Нечитос - @vincitorelepre<br>Ванек - @wixight</center></html>";
        String aboutEn = "<html><center>We are student friends who made this<br>masterpiece project for you.<br><br><b>Our Telegrams for contact:</b><br>Nechitos - @vincitorelepre<br>Vanek - @wixight</center></html>";
        LANG_MAP.put("about.text", new String[]{aboutEn, aboutRu});
    }

    private static String T(String key) {
        String[] val = LANG_MAP.get(key);
        if (val == null) return key;
        return isRussian ? val[1] : val[0];
    }

    // --- UI КОМПОНЕНТЫ ---
    static class RoundedButton extends JButton {
        private int radius = 20;
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setFont(FONT_BOLD); setForeground(Color.WHITE); setBackground(CLR_ACCENT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override public void setEnabled(boolean b) {
            super.setEnabled(b); setBackground(b ? CLR_ACCENT : Color.GRAY);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2); g2.dispose();
        }
    }

    static class RoundedPanel extends JPanel {
        private int radius = 30; private Color bgColor;
        public RoundedPanel(Color color) { this.bgColor = color; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2); g2.dispose();
        }
    }

    // --- SIDEBAR ---
    private static JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBackground(CLR_BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel logo = new JLabel("WinKit");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(isDarkTheme ? Color.WHITE : CLR_ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(40));

        addSidebarHeader(sidebar, T("sidebar.menu"));
        addSidebarLink(sidebar, T("sidebar.home"), "HOME");
        sidebar.add(Box.createVerticalStrut(20));
        
        addSidebarHeader(sidebar, T("sidebar.settings"));
        addSidebarLink(sidebar, T("sidebar.theme") + " / " + T("sidebar.lang"), "SETTINGS");
        
        sidebar.add(Box.createVerticalStrut(20));
        addSidebarHeader(sidebar, T("sidebar.info"));
        addSidebarLink(sidebar, T("sidebar.about"), "ABOUT");
        addSidebarLink(sidebar, T("sidebar.donate"), "DONATE"); // Кнопка Поддержать нас

        return sidebar;
    }

    private static void addSidebarHeader(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(isDarkTheme ? new Color(150, 150, 150) : new Color(100, 100, 100));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l); p.add(Box.createVerticalStrut(10));
    }

    private static void addSidebarLink(JPanel p, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 35));
        btn.setForeground(CLR_TEXT);
        btn.setBackground(CLR_BG_SIDEBAR);
        btn.setBorder(null); btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(FONT_MAIN);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(isDarkTheme ? Color.WHITE : CLR_ACCENT); }
            public void mouseExited(MouseEvent e) { btn.setForeground(CLR_TEXT); }
        });
        btn.addActionListener(e -> cardLayout.show(mainContent, cardName));
        p.add(btn); p.add(Box.createVerticalStrut(5));
    }

    // --- HOME ---
    private static JPanel createHomeTiles() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(CLR_BG_MAIN);
        JPanel grid = new JPanel(new GridLayout(2, 2, 25, 25));
        grid.setBackground(CLR_BG_MAIN);
        grid.setPreferredSize(new Dimension(680, 400)); 
        grid.add(createTile(T("tile.pipette"), "🎨", "PIPETTE"));
        grid.add(createTile(T("tile.archiver"), "📚", "ARCHIVER"));
        grid.add(createTile(T("tile.converter"), "🔄", "CONVERTER"));
        grid.add(createTile(T("tile.notes"), "📝", "NOTES"));
        wrapper.add(grid);
        return wrapper;
    }

    private static JButton createTile(String title, String icon, String card) {
        JButton btn = new JButton("<html><center><span style='font-size:42px'>" + icon + "</span><br><br><nobr><span style='font-size:16px'>" + title + "</span></nobr></center></html>") {
             @Override protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 if (getModel().isRollover()) g2.setColor(isDarkTheme ? Color.WHITE : new Color(230, 240, 255));
                 else g2.setColor(CLR_TILE);
                 g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                 super.paintComponent(g2); g2.dispose();
             }
        };
        btn.setForeground(CLR_TILE_TEXT);
        btn.setContentAreaFilled(false); btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(mainContent, card));
        return btn;
    }

    // --- НОВЫЙ МОДУЛЬ: ПОДДЕРЖАТЬ НАС ---
    private static JPanel createDonatePanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        
        RoundedPanel panel = new RoundedPanel(CLR_PANEL_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        JLabel title = new JLabel(T("donate.title"));
        title.setForeground(CLR_TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel desc = new JLabel(T("donate.desc"));
        desc.setForeground(CLR_TEXT);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Кнопка, открывающая браузер
        RoundedButton btnDonate = new RoundedButton(T("donate.btn"));
        btnDonate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDonate.setPreferredSize(new Dimension(250, 50));
        btnDonate.setMaximumSize(new Dimension(250, 50));
        btnDonate.setBackground(new Color(245, 124, 0)); // Оранжевый цвет DonationAlerts

        btnDonate.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(DONATE_URL));
                } else {
                    JOptionPane.showMessageDialog(frame, "Browser not supported. Link: " + DONATE_URL);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error opening link: " + ex.getMessage());
            }
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(desc);
        panel.add(Box.createVerticalStrut(40));
        panel.add(btnDonate);
        
        container.add(panel);
        return container;
    }

    // --- МОДУЛЬ ПИПЕТКИ ---
    private static JPanel createPipettePanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        RoundedPanel card = new RoundedPanel(CLR_PANEL_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 60, 30, 60));
        
        JLabel colorPreview = new JLabel(" ");
        colorPreview.setOpaque(true);
        colorPreview.setBackground(Color.BLACK);
        colorPreview.setPreferredSize(new Dimension(120, 80));
        colorPreview.setMaximumSize(new Dimension(120, 80));
        colorPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        colorPreview.setBorder(BorderFactory.createLineBorder(isDarkTheme ? Color.WHITE : Color.BLACK, 2));

        JLabel hexLabel = new JLabel("#000000");
        hexLabel.setForeground(CLR_TEXT);
        hexLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        hexLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel copyPanel = new JPanel(new FlowLayout());
        copyPanel.setOpaque(false);
        RoundedButton btnCopyHex = new RoundedButton(T("pipette.copyHex"));
        btnCopyHex.setPreferredSize(new Dimension(120, 35));
        btnCopyHex.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCopyHex.setBackground(new Color(60, 140, 60)); 
        
        RoundedButton btnCopyRgb = new RoundedButton(T("pipette.copyRgb"));
        btnCopyRgb.setPreferredSize(new Dimension(120, 35));
        btnCopyRgb.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCopyRgb.setBackground(new Color(60, 140, 60));

        copyPanel.add(btnCopyHex);
        copyPanel.add(btnCopyRgb);

        RoundedButton btnPick = new RoundedButton(T("pipette.btn"));
        btnPick.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPick.setPreferredSize(new Dimension(200, 50));
        btnPick.setMaximumSize(new Dimension(200, 50));
        
        final Color[] lastColor = {Color.BLACK};

        btnPick.addActionListener(e -> startScreenPicker(colorPreview, hexLabel, lastColor));

        btnCopyHex.addActionListener(e -> {
            String hex = String.format("#%02X%02X%02X", lastColor[0].getRed(), lastColor[0].getGreen(), lastColor[0].getBlue());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(hex), null);
            JOptionPane.showMessageDialog(frame, "HEX copied: " + hex);
        });

        btnCopyRgb.addActionListener(e -> {
            String rgb = lastColor[0].getRed() + ", " + lastColor[0].getGreen() + ", " + lastColor[0].getBlue();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(rgb), null);
            JOptionPane.showMessageDialog(frame, "RGB copied: " + rgb);
        });
        
        card.add(colorPreview); card.add(Box.createVerticalStrut(20));
        card.add(hexLabel); card.add(Box.createVerticalStrut(10));
        card.add(copyPanel); card.add(Box.createVerticalStrut(30));
        card.add(btnPick);
        container.add(card);
        return container;
    }

    // --- МОДУЛЬ ЗАМЕТОК ---
    private static ArrayList<String> savedNotes = new ArrayList<>();

    private static JPanel createNotesPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(CLR_BG_MAIN);
        container.setBorder(new EmptyBorder(10, 20, 20, 20));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(CLR_BG_MAIN);

        RoundedButton btnAdd = new RoundedButton("+ " + T("notes.new"));
        btnAdd.setPreferredSize(new Dimension(160, 35));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));

        RoundedButton btnClose = new RoundedButton("x " + T("notes.close"));
        btnClose.setBackground(new Color(180, 60, 60));
        btnClose.setPreferredSize(new Dimension(160, 35));
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));

        toolbar.add(btnAdd);
        toolbar.add(btnClose);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        if (savedNotes.isEmpty()) {
            savedNotes.add("Welcome to WinKit Notes!");
        }
        
        for (int i = 0; i < savedNotes.size(); i++) {
            addNoteTab(tabbedPane, "Note " + (i + 1), savedNotes.get(i));
        }
        
        btnAdd.addActionListener(e -> {
            addNoteTab(tabbedPane, "Note " + (tabbedPane.getTabCount() + 1), "");
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        });

        btnClose.addActionListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx != -1) {
                tabbedPane.remove(idx);
            }
        });

        container.add(toolbar, BorderLayout.NORTH);
        container.add(tabbedPane, BorderLayout.CENTER);
        
        tabbedPane.addChangeListener(e -> syncNotes(tabbedPane));

        return container;
    }

    private static void addNoteTab(JTabbedPane tabs, String title, String content) {
        JTextArea area = new JTextArea(content);
        area.setFont(new Font("Consolas", Font.PLAIN, 16));
        area.setBackground(isDarkTheme ? new Color(30, 30, 30) : Color.WHITE);
        area.setForeground(isDarkTheme ? Color.WHITE : Color.BLACK);
        area.setCaretColor(isDarkTheme ? Color.WHITE : Color.BLACK);
        area.setLineWrap(true);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        tabs.addTab(title, scroll);
    }
    
    private static void syncNotes(JTabbedPane tabs) {
        savedNotes.clear();
        for (int i=0; i<tabs.getTabCount(); i++) {
            JScrollPane scroll = (JScrollPane) tabs.getComponentAt(i);
            JTextArea area = (JTextArea) scroll.getViewport().getView();
            savedNotes.add(area.getText());
        }
    }

    // --- РАЗДЕЛ О НАС ---
    private static JPanel createAboutPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        RoundedPanel panel = new RoundedPanel(CLR_PANEL_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel(T("sidebar.about"));
        title.setForeground(CLR_TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel content = new JLabel(T("about.text"));
        content.setForeground(CLR_TEXT);
        content.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(title); panel.add(Box.createVerticalStrut(30));
        panel.add(content);
        container.add(panel);
        return container;
    }

    // --- НАСТРОЙКИ ---
    private static JPanel createSettingsPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        RoundedPanel panel = new RoundedPanel(CLR_PANEL_BG);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setPreferredSize(new Dimension(500, 350));

        JLabel title = new JLabel(T("sidebar.settings"));
        title.setForeground(CLR_TEXT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTheme = new JLabel(T("set.theme"));
        lblTheme.setForeground(CLR_TEXT);
        lblTheme.setAlignmentX(Component.CENTER_ALIGNMENT);
        JRadioButton rbDark = new JRadioButton(T("set.dark"));
        JRadioButton rbLight = new JRadioButton(T("set.light"));
        ButtonGroup gpTheme = new ButtonGroup(); gpTheme.add(rbDark); gpTheme.add(rbLight);
        rbDark.setSelected(isDarkTheme); rbLight.setSelected(!isDarkTheme);
        styleRadio(rbDark); styleRadio(rbLight);
        JPanel themePanel = new JPanel(); themePanel.setOpaque(false);
        themePanel.add(rbDark); themePanel.add(rbLight);
        themePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLang = new JLabel(T("set.lang"));
        lblLang.setForeground(CLR_TEXT); lblLang.setAlignmentX(Component.CENTER_ALIGNMENT);
        JRadioButton rbEn = new JRadioButton("English");
        JRadioButton rbRu = new JRadioButton("Русский");
        ButtonGroup gpLang = new ButtonGroup(); gpLang.add(rbEn); gpLang.add(rbRu);
        rbRu.setSelected(isRussian); rbEn.setSelected(!isRussian);
        styleRadio(rbEn); styleRadio(rbRu);
        JPanel langPanel = new JPanel(); langPanel.setOpaque(false);
        langPanel.add(rbEn); langPanel.add(rbRu);
        langPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton btnApply = new RoundedButton("OK");
        btnApply.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnApply.setPreferredSize(new Dimension(100, 40));
        
        btnApply.addActionListener(e -> {
            boolean needReload = false;
            if (rbDark.isSelected() != isDarkTheme) { isDarkTheme = rbDark.isSelected(); applyThemeColors(); needReload = true; }
            if (rbRu.isSelected() != isRussian) { isRussian = rbRu.isSelected(); needReload = true; }
            if (needReload) reloadUI("SETTINGS");
        });

        panel.add(title); panel.add(Box.createVerticalStrut(30));
        panel.add(lblTheme); panel.add(themePanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblLang); panel.add(langPanel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnApply);
        container.add(panel);
        return container;
    }

    private static void styleRadio(JRadioButton rb) {
        rb.setOpaque(false); rb.setForeground(CLR_TEXT); rb.setFont(FONT_MAIN); rb.setFocusPainted(false);
    }

    // --- АРХИВАТОР ---
    private static JPanel createArchiverPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        RoundedPanel card = new RoundedPanel(CLR_PANEL_BG);
        card.setLayout(new GridLayout(2, 1, 20, 20));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(400, 250));

        RoundedButton btnPack = new RoundedButton(T("arch.zip"));
        btnPack.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(); fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                JFileChooser saveFc = new JFileChooser(); saveFc.setSelectedFile(new File("archive.zip"));
                if (saveFc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(saveFc.getSelectedFile()))) {
                        for (File file : files) {
                            FileInputStream fis = new FileInputStream(file);
                            ZipEntry zipEntry = new ZipEntry(file.getName());
                            zos.putNextEntry(zipEntry);
                            byte[] bytes = new byte[1024]; int length;
                            while ((length = fis.read(bytes)) >= 0) zos.write(bytes, 0, length);
                            zos.closeEntry(); fis.close();
                        }
                        JOptionPane.showMessageDialog(frame, T("arch.success"));
                    } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); }
                }
            }
        });

        RoundedButton btnUnpack = new RoundedButton(T("arch.unzip"));
        btnUnpack.setBackground(new Color(60, 150, 100));
        btnUnpack.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(); fc.setFileFilter(new FileNameExtensionFilter("ZIP Archives", "zip"));
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File zipFile = fc.getSelectedFile();
                JFileChooser dirFc = new JFileChooser(); dirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (dirFc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
                        File destDir = dirFc.getSelectedFile(); ZipEntry zipEntry = zis.getNextEntry();
                        byte[] buffer = new byte[1024];
                        while (zipEntry != null) {
                            File newFile = new File(destDir, zipEntry.getName());
                            FileOutputStream fos = new FileOutputStream(newFile); int len;
                            while ((len = zis.read(buffer)) > 0) fos.write(buffer, 0, len);
                            fos.close(); zipEntry = zis.getNextEntry();
                        }
                        JOptionPane.showMessageDialog(frame, T("arch.done"));
                    } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); }
                }
            }
        });
        card.add(btnPack); card.add(btnUnpack);
        container.add(card);
        return container;
    }

    // --- КОНВЕРТЕР ---
    private static JPanel createConverterPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(CLR_BG_MAIN);
        RoundedPanel card = new RoundedPanel(CLR_PANEL_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(500, 350));
        
        JLabel info = new JLabel(T("conv.title"));
        info.setForeground(CLR_TEXT); info.setFont(new Font("Segoe UI", Font.BOLD, 18));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel fileStatus = new JLabel(T("conv.nofile"));
        fileStatus.setForeground(isDarkTheme ? Color.LIGHT_GRAY : Color.GRAY);
        fileStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        RoundedButton btnLoad = new RoundedButton(T("conv.load"));
        btnLoad.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblFormat = new JLabel(T("conv.format"));
        lblFormat.setForeground(CLR_TEXT);
        lblFormat.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[] formats = {"PNG", "JPG", "BMP"};
        JComboBox<String> formatBox = new JComboBox<>(formats);
        formatBox.setMaximumSize(new Dimension(150, 30));
        
        RoundedButton btnSave = new RoundedButton(T("conv.save"));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setBackground(isDarkTheme ? new Color(80, 80, 80) : Color.LIGHT_GRAY);
        btnSave.setEnabled(false);
        
        final File[] sourceFile = {null};
        btnLoad.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(); fc.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp"));
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                sourceFile[0] = fc.getSelectedFile();
                fileStatus.setText(T("conv.selected") + sourceFile[0].getName());
                fileStatus.setForeground(new Color(0, 180, 0));
                btnSave.setEnabled(true); btnSave.setBackground(CLR_ACCENT);
            }
        });
        
        btnSave.addActionListener(e -> {
            if (sourceFile[0] == null) return;
            String targetExt = (String) formatBox.getSelectedItem();
            JFileChooser saveFc = new JFileChooser();
            String baseName = sourceFile[0].getName().replaceFirst("[.][^.]+$", "");
            saveFc.setSelectedFile(new File(baseName + "_new." + targetExt.toLowerCase()));
            if (saveFc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage img = ImageIO.read(sourceFile[0]);
                    if (targetExt.equals("JPG")) {
                        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
                        newImg.createGraphics().drawImage(img, 0, 0, Color.WHITE, null); img = newImg;
                    }
                    ImageIO.write(img, targetExt, saveFc.getSelectedFile());
                    JOptionPane.showMessageDialog(frame, "Success!");
                } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage()); }
            }
        });

        card.add(info); card.add(Box.createVerticalStrut(20));
        card.add(btnLoad); card.add(Box.createVerticalStrut(10));
        card.add(fileStatus); card.add(Box.createVerticalStrut(20));
        JPanel fp = new JPanel(); fp.setOpaque(false); fp.add(lblFormat); fp.add(formatBox);
        card.add(fp); card.add(Box.createVerticalStrut(20));
        card.add(btnSave);
        container.add(card);
        return container;
    }

    // --- ЛОГИКА ЗАХВАТА ЭКРАНА ---
    private static void startScreenPicker(JLabel colorBox, JLabel textLabel, Color[] lastColor) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);
            JWindow overlay = new JWindow();
            overlay.setBounds(screenRect); overlay.setAlwaysOnTop(true);
            overlay.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            JPanel content = new JPanel() {
                Point mousePos = new Point(0, 0);
                {
                    addMouseMotionListener(new MouseMotionAdapter() { public void mouseMoved(MouseEvent e) { mousePos = e.getPoint(); repaint(); } });
                    addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            Color c = new Color(screenCapture.getRGB(e.getX(), e.getY()));
                            lastColor[0] = c;
                            String hex = String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
                            colorBox.setBackground(c); textLabel.setText(hex);
                            overlay.dispose();
                        }
                    });
                }
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(screenCapture, 0, 0, null);
                    int size = 120; int x = mousePos.x + 20; int y = mousePos.y + 20;
                    if (x+size > getWidth()) x = mousePos.x - size - 20; if (y+size > getHeight()) y = mousePos.y - size - 20;
                    BufferedImage sub = screenCapture.getSubimage(Math.max(0, mousePos.x-15), Math.max(0, mousePos.y-15), Math.min(30, screenCapture.getWidth()-mousePos.x), Math.min(30, screenCapture.getHeight()-mousePos.y));
                    Graphics2D g2 = (Graphics2D)g; g2.setStroke(new BasicStroke(2));
                    g2.setColor(Color.BLACK); g2.fillRect(x,y,size,size);
                    g2.drawImage(sub,x,y,size,size,null);
                    g2.setColor(Color.WHITE); g2.drawRect(x,y,size,size);
                    g2.setColor(Color.RED); g2.drawLine(x+size/2,y,x+size/2,y+size); g2.drawLine(x,y+size/2,x+size,y+size/2);
                }
            };
            overlay.add(content); overlay.setVisible(true);
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}