use world;
select distinct cr.Name as Country, cl.Language as Official_Language
from country cr, countrylanguage cl
where cl.CountryCode = cr.Code and cr.Continent = 'Europe' and cl.IsOfficial = 'T'
order by Country, Official_Language
;