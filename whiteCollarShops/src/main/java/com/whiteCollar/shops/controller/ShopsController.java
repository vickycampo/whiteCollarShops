package com.whiteCollar.shops.controller;

import com.whiteCollar.shops.data.Shops;
import com.whiteCollar.shops.data.ShopsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/shops") // This means URL's start with /demo (after Application path)
public class ShopsController
{
    @Autowired // This means to get the bean called shopsRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ShopsRepository shopsRepository;
    private String SubmitMessage = "";
    private String MessageType = "success";

    @PostMapping
    //@ResponseBody
    public String addNewShop (@RequestParam String name , @RequestParam String capacity, Model model)
    {

        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        int capacityInteger;
        if ( name == "")
        {
            MessageType = "error";
            SubmitMessage = "The Shop name cannot be blanck.";
            model.addAttribute("FormName" , name);
            model.addAttribute("FormCapacity" , capacity);
            return ( shopHome( model ) );
        }
        else
        {
            try
            {
                capacityInteger = Integer.parseInt(capacity);
                if ( capacityInteger < 1 )
                {
                    throw (new Exception(""));
                }
            }
            catch ( Exception e )
            {
                MessageType = "error";
                SubmitMessage = "The Capacity of the Shop has to be a valid number greater than 0.";
                model.addAttribute("FormName" , name);
                model.addAttribute("FormCapacity" , 0);
                return ( shopHome( model ) );
            }

        }
        if ( (name != "") && (capacityInteger > 0) )
        {
            //buscamos una tienda con ese nombre
            List<Shops> foundShops = shopsRepository.findByNameIgnoreCase(name);
            if ( foundShops.size() > 0)
            {
                MessageType = "error";
                SubmitMessage = "The Shop already Existed";
                model.addAttribute("FormName" , name);
                model.addAttribute("FormCapacity" , capacity);
                return ( shopHome( model ) );
            }
            Shops shop = new Shops();
            shop.setName(name);
            shop.setCapacity(capacityInteger);
            shopsRepository.save(shop);
            SubmitMessage = "Shop Saved";
            model.addAttribute("FormName" , "");
            model.addAttribute("FormCapacity" , "");
            return ( shopHome( model ) );
        }
        return ( shopHome( model ) );

    }

    @GetMapping
    public String shopHome(Model model)
    {
        /* We fill out the previous form */
        if ( ! model.containsAttribute( "FormName")  )
        {
            model.addAttribute("FormName" , "");
            model.addAttribute("FormCapacity" , "");
        }
        model.addAttribute("messageType", MessageType);
        model.addAttribute("message", SubmitMessage);
        SubmitMessage = "";
        MessageType = "success";
        List<Shops> shops = new ArrayList<>();
        shopsRepository.findAll().forEach(i -> shops.add(i));
        model.addAttribute("ShopsList", shops);
        return "shops";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Shops> getAllShops() {
        // This returns a JSON or XML with the shops
        return shopsRepository.findAll();
    }

}
