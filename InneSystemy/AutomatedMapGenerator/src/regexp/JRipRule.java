package regexp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JRipRule {
	public static void main(String[] args) {
		JRipRule jrr = new JRipRule();
		jrr.test();
	}

	public void parseRule(String rule) {
		String start = rule.substring(0, rule.indexOf("=>"));
		System.out.println("###############");
		System.out.println(start);
		List<String> roads = new ArrayList<String>();

		for (String road : start.split("and")) {
			road = road.trim();
			if (!"".equals(road)) {
				String substring = road.substring(1, road.length() - 1);
				roads.add(substring);
				System.out.println("|" + substring + "|");
			}
		}
	}

	public void test() {
		ArrayList<String> array = new ArrayList<String>();
		array.add("(tissues-paper prd = t) and (sauces-gravy-pkle = t) and (frozen foods = t) and (small goods = t) => total=high (334.0/42.0)");
		array.add("(tissues-paper prd = t) and (sauces-gravy-pkle = t) and (laundry needs = t) and (juice-sat-cord-ms = t) => total=high (310.0/66.0)");
		array.add("(pet foods = t) and (canned vegetables = t) and (haircare = t) and (cleaners-polishers = t) => total=high (47.0/2.0)");
		array.add("(breakfast food = t) and (pet foods = t) and (deodorants-soap = t) and (vegetables = t) and (poultry = t) => total=high (27.0/2.0)");
		array.add("(sauces-gravy-pkle = t) and (party snack foods = t) and (baking needs = t) and (cleaners-polishers = t) => total=high (155.0/44.0)");
		array.add("(wrapping = t) and (party snack foods = t) and (cheese = t) and (bread and cake = t) and (vegetables = t) and (department137 = t) => total=high (58.0/8.0)");
		array.add("(breakfast food = t) and (deodorants-soap = t) and (confectionary = t) and (baby needs = t) => total=high (20.0/1.0)");
		array.add("(sauces-gravy-pkle = t) and (deli gourmet = t) => total=high (48.0/23.0)");
		array.add("(wrapping = t) and (bread and cake = t) and (tissues-paper prd = t) and (dairy foods = t) and (dental needs = t) => total=high (23.0/5.0)");
		array.add("(frozen foods = t) and (poultry = t) and (cleaners-polishers = t) and (dairy foods = t) => total=high (22.0/7.0)");
		array.add(" => total=low (3147.0/515.0)");

		for (String s : array) {
			parseRule(s);
		}
	}
}
