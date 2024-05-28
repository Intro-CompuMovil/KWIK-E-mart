package co.kwik_e_mart.Productos

data class Product(
    var id: String = "",  // Unique identifier for each product
    val name: String = "",
    val stock: Int = 0,
    val price: Double = 0.0,
    val shippingCost: Double = 0.0,
    val gerenteId: String = ""  // ID of the gerente who added the product
)
