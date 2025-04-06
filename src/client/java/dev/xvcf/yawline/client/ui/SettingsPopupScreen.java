package dev.xvcf.yawline.client.ui;

import dev.xvcf.yawline.client.MainClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class SettingsPopupScreen extends Screen {

    private final ArrayList<TextFieldWidget> yawFields = new ArrayList<TextFieldWidget>();
    private final ArrayList<TextFieldWidget> pitchFields = new ArrayList<TextFieldWidget>();

    public SettingsPopupScreen() {
        super(Text.of("Set Yaws & Pitches"));
    }

    @Override
    protected void init() {
        // Container-Abmessungen und Position
        int containerWidth = 400;
        int containerHeight = 250;
        int containerX = (this.width - containerWidth) / 2;
        int containerY = (this.height - containerHeight) / 2;

        // Startposition für Eingabefelder (unter der Überschrift)
        int startY = containerY + 30;

        // Erstelle 5 Paare von Textfeldern
        for (int i = 0; i < 5; i++) {
            int y = startY + i * 40;

            TextFieldWidget yawField = new TextFieldWidget(this.textRenderer, containerX + 100, y, 100, 20, Text.of(String.valueOf(MainClient.yawValues[i])));
            yawField.setText(String.valueOf(MainClient.yawValues[i]));
            this.addDrawableChild(yawField);
            yawFields.add(yawField);

            TextFieldWidget pitchField = new TextFieldWidget(this.textRenderer, containerX + 250, y, 100, 20, Text.of(String.valueOf(MainClient.pitchValues[i])));
            pitchField.setText(String.valueOf(MainClient.pitchValues[i]));
            this.addDrawableChild(pitchField);
            pitchFields.add(pitchField);

            MainClient.getInstance().getLogger().console_debug("Created TextField for Yaw and Pitch at pair " + (i + 1));
            MainClient.getInstance().getLogger().console_debug("Yaw: " + MainClient.yawValues[i] + ", Pitch: " + MainClient.pitchValues[i] + " for pair " + (i + 1));
        }

        // "Save & Exit"-Button
        int buttonX = containerX + (containerWidth - 100) / 2; // Zentriert im Container
        int buttonY = containerY + containerHeight - 30; // Nahe dem unteren Rand
        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Save & Exit"), button -> {
            // Speicherlogik
            for (int i = 0; i < 5; i++) {
                String yawText = yawFields.get(i).getText();
                String pitchText = pitchFields.get(i).getText();
                try {
                    float yaw = Float.parseFloat(yawText.replace(",", "."));
                    float pitch = Float.parseFloat(pitchText.replace(",", "."));
                    MainClient.pitchValues[i] = pitch;
                    MainClient.yawValues[i] = yaw;
                    MainClient.getInstance().getLogger().console_debug("Saved Yaw: " + yaw + ", Pitch: " + pitch + " for pair " + (i + 1));
                } catch (NumberFormatException e) {
                    MainClient.getInstance().getLogger().ingame_error("Invalid input for Yaw or Pitch at pair " + (i + 1));
                    return;
                }
            }
            this.close();
            MainClient.getInstance().saveConfig();
        }).dimensions(buttonX, buttonY, 100, 20).build();
        this.addDrawableChild(saveButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Container
        int containerX = (this.width - 400) / 2;
        int containerY = (this.height - 250) / 2;
        context.fill(containerX, containerY, containerX + 400, containerY + 250, 0xEFEFEFFF); // Grau

        // Überschrift
        String title = "Set Yaws & Pitches";
        int titleX = this.width / 2;
        int titleY = containerY + 10;
        context.drawCenteredTextWithShadow(this.textRenderer, title, titleX, titleY, 0xFFFFFF);

        // Beschriftungen für die Paare
        int startY = containerY + 30;
        for (int i = 0; i < 5; i++) {
            int y = startY + i * 40;
            String pairLabel = "Pair " + (i + 1) + ":";
            context.drawTextWithShadow(this.textRenderer, pairLabel, containerX + 10, y + 5, 0xFFFFFFFF);
            context.drawTextWithShadow(this.textRenderer, "Yaw:", containerX + 70, y + 5, 0xFFFFFFFF);
            context.drawTextWithShadow(this.textRenderer, "Pitch:", containerX + 220, y + 5, 0xFFFFFFFF);
        }

        // Render UI-Komponenten
        super.render(context, mouseX, mouseY, delta);
    }
}
