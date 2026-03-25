package fr.gameurduxvi.uhc.scenarios.diamondlimite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class DiamondLimite extends Scenario {

	public DiamondLimite(String sencarioName, String senarioDescription) {
		super(sencarioName, senarioDescription);
	}
	
	public DiamondLimite(String sencarioName, String senarioDescription, ItemStack senarioItem) {
		super(sencarioName, senarioDescription, senarioItem);
	}
	
	@Override
	public void stateChange() {
		if(super.getScencarioName().equals(Main.getScenariosManager().DIAMOND_LIMITE_NAME)) {
			if(super.isActive()) {
				List<Recipe> backup = new ArrayList<Recipe>();
				{
					// Idk why you change scope, but why not
					Iterator<Recipe> a = Main.getInstance().getServer().recipeIterator();
					while(a.hasNext()){
						Recipe recipe = a.next();
						ItemStack result = recipe.getResult();
						if(!result.getType().equals(Material.DIAMOND_BLOCK)) {
							backup.add(recipe);
						}
					}
				}
				Main.getInstance().getServer().clearRecipes();
				for (Recipe r : backup) Main.getInstance().getServer().addRecipe(r);
				
				Main.getScenariosManager().diamond.clear();
			}
			else {
				List<Recipe> backup = new ArrayList<Recipe>();
				{
					// Idk why you change scope, but why not
					Iterator<Recipe> a = Main.getInstance().getServer().recipeIterator();
					while(a.hasNext()){
						Recipe recipe = a.next();
						ItemStack result = recipe.getResult();
						if(!result.getType().equals(Material.DIAMOND_BLOCK)) {
							backup.add(recipe);
						}
					}
				}
				Main.getInstance().getServer().clearRecipes();
				for (Recipe r : backup) Main.getInstance().getServer().addRecipe(r);
				
				ItemStack it = new ItemStack(Material.DIAMOND_BLOCK);
				ShapedRecipe craft = new ShapedRecipe(it);
				craft.shape("DDD", "DDD", "DDD");
				craft.setIngredient('D', Material.DIAMOND_BLOCK);
				Main.getInstance().getServer().addRecipe(craft);
				
				Main.getScenariosManager().diamond.clear();
			}
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
}
