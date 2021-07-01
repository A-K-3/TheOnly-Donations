package me.aglerr.donations.managers;

import me.aglerr.donations.ConfigValue;
import me.aglerr.donations.objects.Product;
import me.aglerr.donations.utils.Utils;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class DonationGoal {

    private static double donationGoal;
    private static double currentDonation;

    public static String getProgressBar(){
        return Utils.getProgressBar(
                (int) currentDonation,
                (int) donationGoal,
                ConfigValue.PROGRESS_BAR_LENGTH,
                ConfigValue.PROGRESS_BAR_SYMBOL,
                ConfigValue.PROGRESS_BAR_COMPLETED_COLOR,
                ConfigValue.PROGRESS_BAR_UNCOMPLETED_COLOR);
    }

    public static String getDonationPercentage(){
        return Common.numberFormat(((currentDonation / donationGoal) * 100));
    }

    public static void handleDonation(Product product){
        // Update the current donation goal first
        currentDonation += product.getPrice();
        // Check if we have reached the donation goal
        if(currentDonation >= donationGoal){
            // Reset the donation goal
            currentDonation = 0;
            // Execute the commands
            ConfigValue.DONATION_GOAL_COMMANDS.forEach(command ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        }
    }

    public static void reloadDonationGoal(){
        donationGoal = ConfigValue.DONATION_GOAL_AMOUNT;
    }

    public static void onLoad(){
        FileConfiguration data = ConfigManager.DATA.getConfig();
        // Initialize the donation goal
        donationGoal = ConfigValue.DONATION_GOAL_AMOUNT;
        // Initialize the current donation
        currentDonation = data.getDouble("donationGoal");
    }

    public static void onSave(){
        CustomConfig data = ConfigManager.DATA;
        FileConfiguration config = data.getConfig();
        // Set the current donation to the data
        config.set("donationGoal", currentDonation);
        // Finally save the config
        data.saveConfig();
    }

    public static String getDonationGoal(){
        return Common.numberFormat(donationGoal);
    }

    public static String getCurrentDonation(){
        return Common.numberFormat(currentDonation);
    }

}
