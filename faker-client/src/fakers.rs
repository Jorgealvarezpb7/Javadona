use fake::{Fake, faker};
use rand::prelude::*;
use std::collections::HashSet;

use crate::models::*;

const FIRST_NAMES: &[&str] = &[
    "Alejandro",
    "Carlos",
    "David",
    "Elena",
    "Fernando",
    "Gloria",
    "Héctor",
    "Isabel",
    "Javier",
    "Laura",
    "Miguel",
    "Natalia",
    "Óscar",
    "Patricia",
    "Rafael",
    "Silvia",
    "Tomás",
    "Valeria",
    "Xavi",
    "Yolanda",
];

const LAST_NAMES: &[&str] = &[
    "García",
    "Martínez",
    "López",
    "Sánchez",
    "González",
    "Fernández",
    "Rodríguez",
    "Pérez",
    "Gómez",
    "Martín",
    "Jiménez",
    "Ruiz",
    "Hernández",
    "Díaz",
    "Moreno",
    "Álvarez",
    "Muñoz",
    "Romero",
    "Alonso",
    "Gutiérrez",
];

const LOCATIONS: &[(&str, &str, &str)] = &[
    ("Madrid", "Madrid", "28"),
    ("Barcelona", "Barcelona", "08"),
    ("Valencia", "Valencia", "46"),
    ("Sevilla", "Sevilla", "41"),
    ("Zaragoza", "Zaragoza", "50"),
    ("Málaga", "Málaga", "29"),
    ("Murcia", "Murcia", "30"),
    ("Palma", "Baleares", "07"),
    ("Las Palmas", "Las Palmas", "35"),
    ("Bilbao", "Bizkaia", "48"),
    ("Alicante", "Alicante", "03"),
    ("Córdoba", "Córdoba", "14"),
    ("Valladolid", "Valladolid", "47"),
    ("Vigo", "Pontevedra", "36"),
    ("Gijón", "Asturias", "33"),
];

const STREET_TYPES: &[&str] = &["Calle", "Avenida", "Plaza", "Paseo", "Ronda"];
const STREET_NAMES: &[&str] = &[
    "Mayor",
    "Real",
    "Nueva",
    "del Sol",
    "de la Paz",
    "de España",
    "de la Constitución",
    "Gran Vía",
    "del Prado",
    "de Colón",
    "de la Libertad",
    "de Cervantes",
    "de la Reina",
    "del Rey",
];

const STORE_ADJECTIVES: &[&str] = &[
    "Central",
    "Norte",
    "Sur",
    "Este",
    "Oeste",
    "Gran",
    "Nuevo",
    "Principal",
    "Mercado",
    "City",
];

type Item = (&'static str, &'static str, f64);

const DAIRY: &[Item] = &[
    (
        "Leche Entera Pascual 1L",
        "Leche entera de vaca pasteurizada",
        0.89,
    ),
    (
        "Yogur Natural Danone 4x125g",
        "Yogur natural sin azúcar añadido",
        1.29,
    ),
    (
        "Queso Manchego Curado 200g",
        "Queso manchego DOP curado 6 meses",
        3.45,
    ),
    (
        "Mantequilla Larsa 250g",
        "Mantequilla con sal de vaca",
        2.10,
    ),
    (
        "Nata Cocinar 18% 200ml",
        "Nata líquida para cocinar baja en grasa",
        1.15,
    ),
    (
        "Queso Fresco Batido 500g",
        "Queso fresco batido 0% materia grasa",
        1.59,
    ),
];

const BAKERY: &[Item] = &[
    ("Pan de Pueblo 500g", "Pan de masa madre artesanal", 1.75),
    (
        "Baguette Francesa 250g",
        "Baguette crujiente horneada a diario",
        0.65,
    ),
    (
        "Croissants Mantequilla x4",
        "Croissants de mantequilla hojaldrados",
        2.20,
    ),
    (
        "Pan de Molde Integral 500g",
        "Pan de molde 100% integral sin corteza",
        1.89,
    ),
    (
        "Magdalenas Caseras x12",
        "Magdalenas estilo casero con aceite oliva",
        1.59,
    ),
    (
        "Bizcochos Soletilla x20",
        "Soletillas clásicas para postres",
        1.35,
    ),
];

const PRODUCE: &[Item] = &[
    ("Tomates Rama 500g", "Tomates en rama de temporada", 1.20),
    (
        "Lechuga Romana ECO",
        "Lechuga romana ecológica certificada",
        0.99,
    ),
    ("Manzanas Fuji 1kg", "Manzanas Fuji calibre 70-75mm", 2.35),
    (
        "Plátanos Canarias 1kg",
        "Plátanos de Canarias con IGP",
        1.90,
    ),
    (
        "Zanahorias Baby 200g",
        "Zanahorias baby listas para consumir",
        1.10,
    ),
    (
        "Pimientos Rojos 500g",
        "Pimientos rojos para asar o en crudo",
        1.45,
    ),
];

const MEAT: &[Item] = &[
    (
        "Pechuga Pollo Fileteada 500g",
        "Pechuga de pollo de corral fileteada",
        4.50,
    ),
    (
        "Ternera Picada 400g",
        "Carne picada de ternera 100% natural",
        3.99,
    ),
    (
        "Lomo de Cerdo Filetes 300g",
        "Lomo de cerdo ibérico en filetes finos",
        3.20,
    ),
    (
        "Jamón Serrano Loncheado 100g",
        "Jamón serrano curado 18 meses loncheado",
        2.75,
    ),
    (
        "Salchichas Frescas de Cerdo 400g",
        "Salchichas de cerdo frescas con hierbas",
        2.49,
    ),
];

const BEVERAGES: &[Item] = &[
    (
        "Agua Mineral Bezoya 1.5L",
        "Agua mineral natural baja en sodio",
        0.45,
    ),
    ("Coca-Cola Original 2L", "Refresco de cola original", 1.99),
    (
        "Zumo Naranja Don Simón 1L",
        "Zumo de naranja 100% natural sin azúcar",
        1.65,
    ),
    (
        "Cerveza Estrella Damm 6x33cl",
        "Cerveza rubia lager pack 6 latas",
        4.20,
    ),
    (
        "Café Molido Marcilla 250g",
        "Café molido mezcla 50/50 tueste natural",
        3.10,
    ),
    (
        "Leche de Avena 1L",
        "Bebida de avena sin gluten ni lactosa",
        1.39,
    ),
];

const FROZEN: &[Item] = &[
    (
        "Pizza Margarita Dr. Oetker",
        "Pizza margarita con mozzarella 345g",
        2.99,
    ),
    (
        "Guisantes Finos 750g",
        "Guisantes finos congelados al natural",
        1.45,
    ),
    (
        "Croquetas Jamón Ibérico 500g",
        "Croquetas caseras de jamón ibérico",
        3.80,
    ),
    (
        "Helado Vainilla 460ml",
        "Helado cremoso de vainilla natural",
        5.50,
    ),
    (
        "Patatas Fritas Horno 750g",
        "Patatas para horno sin aceite añadido",
        2.25,
    ),
];

const PERSONAL_CARE: &[Item] = &[
    (
        "Champú Elvive Nutrición 400ml",
        "Champú nutrición extraordinaria L'Oreal",
        4.99,
    ),
    (
        "Gel Ducha Nivea 500ml",
        "Gel de ducha hidratante con vitamina E",
        2.89,
    ),
    (
        "Pasta Dental Colgate 75ml",
        "Pasta de dientes triple acción blanqueadora",
        1.99,
    ),
    (
        "Desodorante Dove 48h 150ml",
        "Desodorante antitranspirante roll-on",
        2.45,
    ),
    (
        "Crema Hidratante Neutrogena",
        "Crema hidratante cara y cuerpo 200ml",
        6.99,
    ),
];

const CLEANING: &[Item] = &[
    (
        "Ariel Pods 3en1 x25",
        "Detergente en cápsulas para lavadora",
        8.99,
    ),
    (
        "Limpiahogar Pino 1L",
        "Limpiahogar multiusos aroma pino",
        1.59,
    ),
    (
        "Bayetas Vileda x5",
        "Bayetas multiusos antibacterianas",
        3.49,
    ),
    (
        "Fairy Original 780ml",
        "Lavavajillas original extra concentrado",
        2.99,
    ),
    (
        "Papel Cocina Scottex x3",
        "Papel de cocina doble hoja absorbente",
        2.79,
    ),
];

pub fn ean13(rng: &mut impl Rng) -> String {
    let digits: Vec<u32> = (0..12).map(|_| rng.gen_range(0..10u32)).collect();
    let sum: u32 = digits
        .iter()
        .enumerate()
        .map(|(i, &d)| if i % 2 == 0 { d } else { d * 3 })
        .sum();
    let check = (10 - (sum % 10)) % 10;
    let mut code: String = digits.iter().map(|d| d.to_string()).collect();
    code.push_str(&check.to_string());
    code
}

pub fn dni(rng: &mut impl Rng) -> String {
    const LETTERS: &str = "TRWAGMYFPDXBNJZSQVHLCKE";
    let num: u32 = rng.gen_range(10_000_000..99_999_999);
    let letter = LETTERS.chars().nth((num % 23) as usize).unwrap();
    format!("{num}{letter}")
}

pub fn nie(rng: &mut impl Rng) -> String {
    const LETTERS: &str = "TRWAGMYFPDXBNJZSQVHLCKE";
    const PREFIXES: &[char] = &['X', 'Y', 'Z'];
    let prefix = *PREFIXES.choose(rng).unwrap();
    let num: u32 = rng.gen_range(1_000_000..9_999_999);
    let prefix_val: u32 = match prefix {
        'X' => 0,
        'Y' => 1,
        _ => 2,
    };
    let letter = LETTERS
        .chars()
        .nth(((prefix_val * 10_000_000 + num) % 23) as usize)
        .unwrap();
    format!("{prefix}{num}{letter}")
}

fn asciify(s: &str) -> String {
    s.chars()
        .map(|c| match c {
            'á' | 'à' | 'â' | 'ä' => 'a',
            'é' | 'è' | 'ê' | 'ë' => 'e',
            'í' | 'ì' | 'î' | 'ï' => 'i',
            'ó' | 'ò' | 'ô' | 'ö' => 'o',
            'ú' | 'ù' | 'û' | 'ü' => 'u',
            'ñ' => 'n',
            'ç' => 'c',
            ' ' => '_',
            c if c.is_ascii_alphanumeric() => c,
            _ => '_',
        })
        .collect()
}

pub fn customer(rng: &mut impl Rng, used_docs: &mut HashSet<String>) -> CreateCustomerRequest {
    let first = *FIRST_NAMES.choose(rng).unwrap();
    let last1 = *LAST_NAMES.choose(rng).unwrap();
    let last2 = *LAST_NAMES.choose(rng).unwrap();
    let (city, province, prefix) = *LOCATIONS.choose(rng).unwrap();

    let (doc_type, doc_value) = loop {
        let (t, v) = if rng.gen_bool(0.85) {
            (DocumentType::Dni, dni(rng))
        } else {
            (DocumentType::Nie, nie(rng))
        };
        if used_docs.insert(v.clone()) {
            break (t, v);
        }
    };

    // Use faker crate for emails
    let email: String = faker::internet::en::SafeEmail().fake_with_rng(rng);
    // Build a domain-branded variant
    let slug = format!(
        "{}.{}{}",
        asciify(&first.to_lowercase()),
        asciify(&last1.to_lowercase()),
        rng.gen_range(1..99u32),
    );
    let domain = email.split('@').nth(1).unwrap_or("gmail.com");
    let email = format!("{slug}@{domain}");

    let street = format!(
        "{} {} {}",
        STREET_TYPES.choose(rng).unwrap(),
        STREET_NAMES.choose(rng).unwrap(),
        rng.gen_range(1..200u32),
    );
    let postal = format!("{}{:03}", prefix, rng.gen_range(0..999u32));
    let birth = format!(
        "{:04}-{:02}-{:02}",
        rng.gen_range(1955u32..2000),
        rng.gen_range(1u32..=12),
        rng.gen_range(1u32..=28),
    );
    let phone = format!(
        "+34 6{:02} {:03} {:03}",
        rng.gen_range(0u32..99),
        rng.gen_range(0u32..999),
        rng.gen_range(0u32..999),
    );

    CreateCustomerRequest {
        first_name: first.to_string(),
        last_name: format!("{last1} {last2}"),
        date_of_birth: birth,
        phone_number: phone,
        document_type: doc_type,
        document_value: doc_value,
        email,
        street,
        city: city.to_string(),
        postal_code: postal,
        province: province.to_string(),
    }
}

pub fn sales_point(rng: &mut impl Rng, index: usize) -> CreateSalesPointRequest {
    let (city, province, prefix) = *LOCATIONS.choose(rng).unwrap();
    let adj = STORE_ADJECTIVES[index % STORE_ADJECTIVES.len()];
    let street = format!(
        "{} {} {}",
        STREET_TYPES.choose(rng).unwrap(),
        STREET_NAMES.choose(rng).unwrap(),
        rng.gen_range(1..200u32),
    );
    let phone = format!(
        "+34 9{:02} {:03} {:03}",
        rng.gen_range(0u32..99),
        rng.gen_range(0u32..999),
        rng.gen_range(0u32..999),
    );
    let open_hour = *[8u8, 9, 10].choose(rng).unwrap();
    let close_hour = *[20u8, 21, 22].choose(rng).unwrap();

    CreateSalesPointRequest {
        name: format!("Javadona {adj} {city}"),
        street,
        city: city.to_string(),
        postal_code: format!("{}{:03}", prefix, rng.gen_range(0..999u32)),
        province: province.to_string(),
        phone_number: phone,
        opens_at: format!("{:02}:00:00", open_hour),
        closes_at: format!("{:02}:00:00", close_hour),
    }
}

pub fn product(rng: &mut impl Rng, used_barcodes: &mut HashSet<String>) -> CreateProductRequest {
    let (category, table): (ProductCategory, &[Item]) = match rng.gen_range(0..8u8) {
        0 => (ProductCategory::Dairy, DAIRY),
        1 => (ProductCategory::Bakery, BAKERY),
        2 => (ProductCategory::Produce, PRODUCE),
        3 => (ProductCategory::Meat, MEAT),
        4 => (ProductCategory::Beverages, BEVERAGES),
        5 => (ProductCategory::Frozen, FROZEN),
        6 => (ProductCategory::PersonalCare, PERSONAL_CARE),
        _ => (ProductCategory::Cleaning, CLEANING),
    };

    let (name, desc, base) = *table.choose(rng).unwrap();
    // ±15 % price variation
    let price = ((base * rng.gen_range(0.85f64..1.15)) * 100.0).round() / 100.0;

    let barcode = loop {
        let b = ean13(rng);
        if used_barcodes.insert(b.clone()) {
            break b;
        }
    };

    // Use faker crate for an occasional enriched description
    let description = if rng.gen_bool(0.4) {
        let extra: String = faker::lorem::en::Sentence(3..6).fake_with_rng(rng);
        Some(format!("{desc}. {extra}"))
    } else {
        Some(desc.to_string())
    };

    CreateProductRequest {
        name: name.to_string(),
        description,
        category,
        barcode_value: barcode,
        base_price: price,
    }
}
