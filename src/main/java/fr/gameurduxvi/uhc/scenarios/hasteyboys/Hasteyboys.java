package fr.gameurduxvi.uhc.scenarios.hasteyboys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import fr.gameurduxvi.uhc.Main;
import fr.gameurduxvi.uhc.SuperClasses.Scenario;

public class Hasteyboys extends Scenario {

	public Hasteyboys(String sencarioName, String senarioDescription) {
		super(sencarioName, senarioDescription);
	}

	public Hasteyboys(String sencarioName, String senarioDescription, ItemStack senarioItem) {
		super(sencarioName, senarioDescription, senarioItem);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void stateChange() {
		if(super.getScencarioName().equals(Main.getScenariosManager().HASTEY_BOYS)) {
			ArrayList<ItemStack> removedItems = new ArrayList<>();
			
			removedItems.add(new ItemStack(Material.WOOD_PICKAXE));
			removedItems.add(new ItemStack(Material.STONE_PICKAXE));
			removedItems.add(new ItemStack(Material.GOLD_PICKAXE));
			removedItems.add(new ItemStack(Material.IRON_PICKAXE));
			removedItems.add(new ItemStack(Material.DIAMOND_PICKAXE));

			removedItems.add(new ItemStack(Material.WOOD_AXE));
			removedItems.add(new ItemStack(Material.STONE_AXE));
			removedItems.add(new ItemStack(Material.GOLD_AXE));
			removedItems.add(new ItemStack(Material.IRON_AXE));
			removedItems.add(new ItemStack(Material.DIAMOND_AXE));

			removedItems.add(new ItemStack(Material.WOOD_SPADE));
			removedItems.add(new ItemStack(Material.STONE_SPADE));
			removedItems.add(new ItemStack(Material.GOLD_SPADE));
			removedItems.add(new ItemStack(Material.IRON_SPADE));
			removedItems.add(new ItemStack(Material.DIAMOND_SPADE));
			
			List<Recipe> backup = new ArrayList<Recipe>();
			{
				// Idk why you change scope, but why not
				Iterator<Recipe> a = Main.getInstance().getServer().recipeIterator();
				while(a.hasNext()){
					Recipe recipe = a.next();
					ItemStack result = recipe.getResult();
					boolean found = false;
					for(ItemStack it: removedItems) {
						if(result.getType().equals(it.getType())) {
							found = true;
						}
					}
					if(!found){
						backup.add(recipe);
					}
				}
			}
			Main.getInstance().getServer().clearRecipes();
			for (Recipe r : backup) Main.getInstance().getServer().addRecipe(r);
			
			ItemStack it;
			ShapedRecipe craft;
			for(int i = 0; i <= 5; i++) {
				it = new ItemStack(Material.WOOD_PICKAXE);
				if(super.isActive()) {
					it.addEnchantment(Enchantment.DIG_SPEED, 3);
					it.addEnchantment(Enchantment.DURABILITY, 3);
				}
				craft = new ShapedRecipe(it);
				craft.shape("III", " S ", " S ");
				craft.setIngredient('I', Material.WOOD, i);
				craft.setIngredient('S', Material.STICK);
		        Main.getInstance().getServer().addRecipe(craft);
		        
		        it = new ItemStack(Material.WOOD_AXE);
				if(super.isActive()) {
					it.addEnchantment(Enchantment.DIG_SPEED, 3);
					it.addEnchantment(Enchantment.DURABILITY, 3);
				}
				craft = new ShapedRecipe(it);
				craft.shape("II ", "IS ", " S ");
				craft.setIngredient('I', Material.WOOD, i);
				craft.setIngredient('S', Material.STICK);
				Main.getInstance().getServer().addRecipe(craft);
		        
		        it = new ItemStack(Material.WOOD_AXE);
				if(super.isActive()) {
					it.addEnchantment(Enchantment.DIG_SPEED, 3);
					it.addEnchantment(Enchantment.DURABILITY, 3);
				}
				craft = new ShapedRecipe(it);
				craft.shape(" II", " SI", " S ");
				craft.setIngredient('I', Material.WOOD, i);
				craft.setIngredient('S', Material.STICK);
				Main.getInstance().getServer().addRecipe(craft);

		        it = new ItemStack(Material.WOOD_SPADE);
				if(super.isActive()) {
					it.addEnchantment(Enchantment.DIG_SPEED, 3);
					it.addEnchantment(Enchantment.DURABILITY, 3);
				}
				craft = new ShapedRecipe(it);
				craft.shape(" I ", " S ", " S ");
				craft.setIngredient('I', Material.WOOD, i);
				craft.setIngredient('S', Material.STICK);
				Main.getInstance().getServer().addRecipe(craft);
			}
			
	        
	        
	        
	        
	        
	        
	        it = new ItemStack(Material.STONE_PICKAXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("III", " S ", " S ");
			craft.setIngredient('I', Material.COBBLESTONE);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.GOLD_PICKAXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("III", " S ", " S ");
			craft.setIngredient('I', Material.GOLD_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.IRON_PICKAXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("III", " S ", " S ");
			craft.setIngredient('I', Material.IRON_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.DIAMOND_PICKAXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("III", " S ", " S ");
			craft.setIngredient('I', Material.DIAMOND);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        
	        
	        
	        
	        
	        it = new ItemStack(Material.STONE_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("II ", "IS ", " S ");
			craft.setIngredient('I', Material.COBBLESTONE);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.GOLD_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("II ", "IS ", " S ");
			craft.setIngredient('I', Material.GOLD_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.IRON_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("II ", "IS ", " S ");
			craft.setIngredient('I', Material.IRON_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.DIAMOND_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape("II ", "IS ", " S ");
			craft.setIngredient('I', Material.DIAMOND);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        
	        
	        
	        
	        
	        
	        it = new ItemStack(Material.STONE_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" II", " SI", " S ");
			craft.setIngredient('I', Material.COBBLESTONE);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.GOLD_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" II", " SI", " S ");
			craft.setIngredient('I', Material.GOLD_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.IRON_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" II", " SI", " S ");
			craft.setIngredient('I', Material.IRON_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.DIAMOND_AXE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" II", " SI", " S ");
			craft.setIngredient('I', Material.DIAMOND);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        
	        
	        
	        
	        
	        it = new ItemStack(Material.STONE_SPADE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" I ", " S ", " S ");
			craft.setIngredient('I', Material.COBBLESTONE);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.GOLD_SPADE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" I ", " S ", " S ");
			craft.setIngredient('I', Material.GOLD_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.IRON_SPADE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" I ", " S ", " S ");
			craft.setIngredient('I', Material.IRON_INGOT);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
	        
	        it = new ItemStack(Material.DIAMOND_SPADE);
			if(super.isActive()) {
				it.addEnchantment(Enchantment.DIG_SPEED, 3);
				it.addEnchantment(Enchantment.DURABILITY, 3);
			}
			craft = new ShapedRecipe(it);
			craft.shape(" I ", " S ", " S ");
			craft.setIngredient('I', Material.DIAMOND);
			craft.setIngredient('S', Material.STICK);
			Main.getInstance().getServer().addRecipe(craft);
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
}
